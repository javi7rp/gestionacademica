package com.iesvdc.acceso.gestionacademica.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iesvdc.acceso.gestionacademica.modelos.Alumno;
import com.iesvdc.acceso.gestionacademica.modelos.Telefono;
import com.iesvdc.acceso.gestionacademica.repos.RepoAlumno;
import com.iesvdc.acceso.gestionacademica.repos.RepoTelefono;

@Controller
@RequestMapping("/alumnos")
public class ControllerAlumno {

    @Autowired
    private RepoAlumno repoAlumno;

    /**
     * Devuelve la lista de alumnos
     */
    @GetMapping(path = "/")
    public String findAll(Model modelo) {
        List<Alumno> alumnos = repoAlumno.findAll();
        modelo.addAttribute("alumnos", alumnos);
        return "alumnos/alumnos";
    }

    /**
     * Devuelve el formulario para añadir un nuevo alumno
     */
    @GetMapping("/add")
    public String addAlumno(Model modelo) {
        modelo.addAttribute("alumno", new Alumno());
        return "alumnos/add";
    }

    /**
     * Recoge los datos del formulario anterior para crear un nuevo alumno
     */
    @PostMapping("/add")
    public String addAlumno(@ModelAttribute("alumno") @NonNull Alumno alumno) {
        repoAlumno.save(alumno);
        return "redirect:/alumnos";
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
        return "alumnos/delete";
    }

    /**
     * Elimina el alumno de la base de datos si es posible
     */
    @PostMapping("/delete/{id}")
    public String deleteAlumno(Model modelo, @PathVariable("id") @NonNull Long id) {
        try {
            repoAlumno.deleteById(id);
        } catch (Exception e) {
            modelo.addAttribute("mensaje", "El alumno no se puede eliminar porque tiene operaciones relacionadas.");
            return "error";
        }
        return "redirect:/alumnos";
    }

    /**
     * Muestra un formulario para editar el alumno
     */
    @GetMapping("/edit/{id}")
    public String editAlumnoForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);
        if (oAlumno.isPresent()) {
            modelo.addAttribute("alumno", oAlumno.get());
            return "alumnos/edit";
        } else {
            modelo.addAttribute("mensaje", "El alumno consultado no existe.");
            return "error";
        }
    }

    /**
     * Guarda los cambios realizados en el formulario de edición del alumno
     */
    @PostMapping("/edit/{id}")
    public String editAlumno(Model modelo, @PathVariable("id") @NonNull Long id, @ModelAttribute("alumno") @NonNull Alumno alumno) {
        alumno.setId(id); // Aseguramos que el ID se establezca correctamente
        repoAlumno.save(alumno);
        return "redirect:/alumnos";
    }

}
