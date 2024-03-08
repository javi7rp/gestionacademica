package com.iesvdc.acceso.gestionacademica.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iesvdc.acceso.gestionacademica.modelos.Alumno;
import com.iesvdc.acceso.gestionacademica.modelos.Asignatura;
import com.iesvdc.acceso.gestionacademica.modelos.Matricula;
import com.iesvdc.acceso.gestionacademica.modelos.Profesor;
import com.iesvdc.acceso.gestionacademica.modelos.Usuario;
import com.iesvdc.acceso.gestionacademica.repos.RepoAlumno;
import com.iesvdc.acceso.gestionacademica.repos.RepoAsignatura;
import com.iesvdc.acceso.gestionacademica.repos.RepoUsuario;

@Controller
@RequestMapping("/alumno")
public class ControllerAlumno {

    @Autowired
    private RepoAlumno repoAlumno;

    @Autowired
    private RepoAsignatura repoAsignatura;

    @Autowired
    private RepoUsuario repoUsuario;

    private final PasswordEncoder encoder;

    public ControllerAlumno(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Devuelve la lista de alumnos
     */
    @GetMapping
    public String listarAlumnos(Model modelo) {
        modelo.addAttribute("alumnos", repoAlumno.findAll());
        return "alumno/alumnos";
    }

    /**
     * Devuelve el formulario para añadir un nuevo alumno
     */
    @GetMapping("/add")
    public String addAlumnoGet(Model modelo) {
        modelo.addAttribute("alumno", new Alumno());
        return "alumno/add";
    }

    /**
     * Recoge los datos del formulario anterior para crear un nuevo alumno
     */
    @PostMapping("/add")
    public String addAlumno(@ModelAttribute("alumno") @NonNull Alumno alumno) {
        alumno.setPassword(encoder.encode(alumno.getPassword()));
        alumno.setAuthority("ALUMNO");
        repoAlumno.save(alumno);

        Usuario usuario = new Usuario();
        usuario.setUsername(alumno.getUsername());
        usuario.setPassword(alumno.getPassword());
        usuario.setAuthority(alumno.getAuthority());
        usuario.setAlumno(alumno);
        repoUsuario.save(usuario);
        return "redirect:/alumno";
    }

    /**
     * Muestra un formulario para confirmar el borrado del alumno
     */
    @GetMapping("/delete/{id}")
    public String deleteAlumnoForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);
        if (oAlumno.isPresent())
            modelo.addAttribute("alumno", oAlumno.get());
        else {
            modelo.addAttribute("mensaje", "El alumno consultado no existe.");
            return "error";
        }
        return "alumno/delete";
    }

    /**
     * Elimina el alumno de la base de datos si es posible
     */
    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteAlumno(@PathVariable("id") @NonNull Long id) {
        repoUsuario.deleteByAlumnoId(id);
        repoAlumno.deleteById(id);
        return "redirect:/alumno";
    }

    /**
     * Muestra un formulario para editar el alumno
     */
    @GetMapping("/edit/{id}")
    public String editAlumnoForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);
        if (oAlumno.isPresent()) {
            modelo.addAttribute("alumno", oAlumno.get());
            return "alumno/edit";
        } else {
            modelo.addAttribute("mensaje", "El alumno consultado no existe.");
            return "error";
        }
    }

    /**
     * Guarda los cambios realizados en el formulario de edición del alumno
     */
    @PostMapping("/edit/{id}")
    public String editAlumno(Model modelo, @PathVariable("id") @NonNull Long id,
            @ModelAttribute("alumnoModificado") @NonNull Alumno alumnoModificado) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);

        if (oAlumno.isPresent()) {
            Alumno alumnoExistente = oAlumno.get();
            Usuario usuarioExistente = repoUsuario.findByAlumnoId(id);

            alumnoExistente.setNombre(alumnoModificado.getNombre());
            alumnoExistente.setApellido(alumnoModificado.getApellido());
            alumnoExistente.setTelefono(alumnoModificado.getTelefono());
            alumnoExistente.setEmail(alumnoModificado.getEmail());
            alumnoExistente.setUsername(alumnoModificado.getUsername());

            usuarioExistente.setUsername(alumnoModificado.getUsername());

            repoAlumno.save(alumnoExistente);
            repoUsuario.save(usuarioExistente);

            return "redirect:/alumno";
        } else {
            return "error";
        }
    }



    /**
     * Asignaturas matriculadas del Alumno
     */
    @GetMapping("/matriculas/{id}")
    public String asignaturasMatriculadas(Model modelo, @PathVariable("id") @NonNull Long id) {
        Alumno alumnoMatriculado;
        if (id != 0) {
            Optional<Alumno> oAlumno = repoAlumno.findById(id);

            if (oAlumno.isPresent()) {
                alumnoMatriculado = oAlumno.get();
            } else {
                modelo.addAttribute("mensaje", "El alumno consultado no existe.");
                return "error";
            }
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nombreAlumnoLogueado = authentication.getName();

            alumnoMatriculado = repoAlumno.findByUsername(nombreAlumnoLogueado);

            if (alumnoMatriculado == null) {
                modelo.addAttribute("mensaje", "El profesor consultado no existe.");
                return "error";
            }
        }

        List<Asignatura> asignaturasMatriculadas = alumnoMatriculado.getAsignaturas();

        modelo.addAttribute("alumno", alumnoMatriculado);
        modelo.addAttribute("asignaturas", asignaturasMatriculadas);
        return "alumno/matriculas";
    }


    /**
     * Matricular al Alumno
     */
    @GetMapping("/matricular/{id}")
    public String matricularAlumnoForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional <Alumno> oAlumno = repoAlumno.findById(id);
        if(oAlumno.isPresent()) {
            modelo.addAttribute("alumno", oAlumno.get());
            modelo.addAttribute("asignaturas", repoAsignatura.findAll());
            modelo.addAttribute("matricula", new Matricula());
            return "alumno/matricular";
        } else {
            modelo.addAttribute("mensaje", "El alumno consultado no existe.");
            return "error";
        }
    }

    @RequestMapping(value = "/matricular/{id}", method = RequestMethod.POST)
    public String matricularAlumno(@PathVariable("id") @NonNull Long idAlumno, @ModelAttribute("matricula") @NonNull Matricula matricula) {
        Optional <Alumno> oAlumno = repoAlumno.findById(idAlumno);
        Long idAsignatura = matricula.getAsignaturaId();

        if(oAlumno.isPresent() && idAsignatura != null){
            Alumno alumnoMatriculado = oAlumno.get();
            Asignatura asignaturaMatriculada = repoAsignatura.findById(idAsignatura).get();

            alumnoMatriculado.getAsignaturas().add(asignaturaMatriculada);
            asignaturaMatriculada.getAlumnos().add(alumnoMatriculado);

            repoAlumno.save(alumnoMatriculado);
            repoAsignatura.save(asignaturaMatriculada);

            return "redirect:/alumno";
        }else{
            return "error";
        }
    }

    @GetMapping("/profesores")
    public String profesoresAsignados(Model modelo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreAlumnoLogueado = authentication.getName();

        Alumno alumnoMatriculado = repoAlumno.findByUsername(nombreAlumnoLogueado);
        List<Asignatura> asignaturasMatriculadas = alumnoMatriculado.getAsignaturas();
        List<Profesor> profesoresAsignados = new ArrayList<>();

        for(int i = 0; i < asignaturasMatriculadas.size(); i++){
            profesoresAsignados.add(asignaturasMatriculadas.get(i).getProfesor());
        }

        modelo.addAttribute("alumno", alumnoMatriculado);
        modelo.addAttribute("asignaturas", asignaturasMatriculadas);
        modelo.addAttribute("profesores", profesoresAsignados);
        return "alumno/profesores";
    }

}
