package com.iesvdc.acceso.gestionacademica.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iesvdc.acceso.gestionacademica.modelos.Asignatura;
import com.iesvdc.acceso.gestionacademica.modelos.Imparte;
import com.iesvdc.acceso.gestionacademica.modelos.Profesor;
import com.iesvdc.acceso.gestionacademica.modelos.Usuario;
import com.iesvdc.acceso.gestionacademica.repos.RepoAsignatura;
import com.iesvdc.acceso.gestionacademica.repos.RepoProfesor;
import com.iesvdc.acceso.gestionacademica.repos.RepoUsuario;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/profesor")
public class ControllerProfesor {

    @Autowired
    private RepoProfesor repoProfesor;

    @Autowired
    private RepoAsignatura repoAsignatura;

    @Autowired
    private RepoUsuario repoUsuario;

    private final PasswordEncoder encoder;

    public ControllerProfesor(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Devuelve la lista de profesores
     */
    @GetMapping
    public String listarProfesores(Model modelo) {
        modelo.addAttribute("profesores", repoProfesor.findAll());
        return "profesor/profesores";
    }

    /**
     * Devuelve el formulario para añadir un nuevo profesor
     */
    @GetMapping("/add")
    public String addProfesorGet(Model modelo) {
        modelo.addAttribute("profesor", new Profesor());
        return "profesor/add";
    }

    @PostMapping("/add")
    public String addProfesor(@ModelAttribute("profesor") @NonNull Profesor profesor) {
        profesor.setPassword(encoder.encode(profesor.getPassword()));
        profesor.setAuthority("PROFESOR");
        repoProfesor.save(profesor);

        Usuario usuario = new Usuario();
        usuario.setUsername(profesor.getUsername());
        usuario.setPassword(profesor.getPassword());
        usuario.setAuthority(profesor.getAuthority());
        usuario.setProfesor(profesor);
        repoUsuario.save(usuario);
        return "redirect:/profesor";
    }

    /**
     * Muestra un formulario para confirmar el borrado del profesor
     */
    @GetMapping("/delete/{id}")
    public String deleteProfesorForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Profesor> objProfesor = repoProfesor.findById(id);
        if (objProfesor.isPresent()) {
            modelo.addAttribute("profesor", objProfesor.get());
            return "profesor/delete";
        } else {
            modelo.addAttribute("mensaje", "El profesor consultado no existe.");
            return "error";
        }
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteProfesor(@PathVariable("id") @NonNull Long id) {
        Optional<Profesor> objProfesor = repoProfesor.findById(id);
        if (objProfesor.isPresent()) {
            Profesor profesor = objProfesor.get();
            // Eliminar todas las asignaturas asociadas al profesor
            repoAsignatura.deleteByProfesor(profesor);
            repoUsuario.deleteByProfesorId(id);
            // Después de eliminar las asignaturas, eliminar al profesor
            repoProfesor.deleteById(id);
            return "redirect:/profesor";
        } else {
            return "error"; // Manejar el caso en que el profesor no existe
        }
    }

    /**
     * Muestra un formulario para editar el profesor
     */
    @GetMapping("/edit/{id}")
    public String editProfesorForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Profesor> objProfesor = repoProfesor.findById(id);
        if (objProfesor.isPresent()) {
            modelo.addAttribute(
                    "profesor", objProfesor.get());
            return "profesor/edit";
        } else {
            modelo.addAttribute("mensaje", "El profesor consultado no existe.");
            return "error";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editProfesor(@PathVariable("id") @NonNull Long id,
            @ModelAttribute("profesorModificado") Profesor profesorModificado) {
        Optional<Profesor> objProfesor = repoProfesor.findById(id);

        if (objProfesor.isPresent()) {
            Profesor profesorExistente = objProfesor.get();
            Usuario usuarioExistente = repoUsuario.findByProfesorId(id);

            profesorExistente.setNombre(profesorModificado.getNombre());
            profesorExistente.setApellido(profesorModificado.getApellido());
            profesorExistente.setEmail(profesorModificado.getEmail());
            profesorExistente.setUsername(profesorModificado.getUsername());

            usuarioExistente.setUsername(profesorModificado.getUsername());

            repoProfesor.save(profesorExistente);
            repoUsuario.save(usuarioExistente);

            return "redirect:/profesor";
        } else {
            return "error";
        }
    }

    // Impartir

    @GetMapping("/impartir/{id}")
    public String asignarProfesorForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Profesor> objProfesor = repoProfesor.findById(id);
        if (objProfesor.isPresent()) {
            modelo.addAttribute("profesor", objProfesor.get());
            modelo.addAttribute("asignaturas", repoAsignatura.findAll());
            modelo.addAttribute("imparte", new Imparte());
            return "profesor/impartir";
        } else {
            modelo.addAttribute("mensaje", "El profesor consultado no existe.");
            return "error";
        }
    }

    @RequestMapping(value = "/impartir/{id}", method = RequestMethod.POST)
    public String asignarProfesor(@PathVariable("id") @NonNull Long idProfesor,
            @ModelAttribute("imparte") @NonNull Imparte imparte) {
        Optional<Profesor> objProfesor = repoProfesor.findById(idProfesor);
        Long idAsignatura = imparte.getAsignaturaId();

        if (objProfesor.isPresent() && idAsignatura != null) {
            Profesor profesorAsignado = objProfesor.get();
            Asignatura asignaturaImpartida = repoAsignatura.findById(idAsignatura).get();

            if (profesorAsignado.getAsignaturas().size() < 2) {
                profesorAsignado.getAsignaturas().add(asignaturaImpartida);
                asignaturaImpartida.setProfesor(profesorAsignado);
                ;

                repoProfesor.save(profesorAsignado);
                repoAsignatura.save(asignaturaImpartida);

                return "redirect:/profesor";
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/imparte/{id}")
    public String asignaturasImpartidas(Model modelo, @PathVariable("id") @NonNull Long id) {
        Profesor profesorAsignado;
        if (id != 0) {
            Optional<Profesor> objProfesor = repoProfesor.findById(id);

            if (objProfesor.isPresent()) {
                profesorAsignado = objProfesor.get();
            } else {
                modelo.addAttribute("mensaje", "El profesor consultado no existe.");
                return "error";
            }
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nombreProfesorLogueado = authentication.getName();

            profesorAsignado = repoProfesor.findByUsername(nombreProfesorLogueado);

            if (profesorAsignado == null) {
                modelo.addAttribute("mensaje", "El profesor consultado no existe.");
                return "error";
            }
        }

        List<Asignatura> asignaturasImpartidas = profesorAsignado.getAsignaturas();

        modelo.addAttribute("profesor", profesorAsignado);
        modelo.addAttribute("asignaturas", asignaturasImpartidas);
        return "profesor/imparte";
    }

}
