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

import com.iesvdc.acceso.gestionacademica.modelos.Profesor;
import com.iesvdc.acceso.gestionacademica.repos.RepoProfesor;

@Controller
@RequestMapping("/profesores")
public class ControllerProfesor {

    @Autowired
    private RepoProfesor repoProfesor;

    /**
     * Devuelve la lista de profesores
     */
    @GetMapping(path = "/")
    public String findAll(Model modelo) {
        List<Profesor> profesores = repoProfesor.findAll();
        modelo.addAttribute("profesores", profesores);
        return "profesores/profesores";
    }

    /**
     * Devuelve el formulario para añadir un nuevo profesor
     */
    @GetMapping("/add")
    public String addProfesor(Model modelo) {
        modelo.addAttribute("profesor", new Profesor());
        return "profesores/add";
    }

    /**
     * Recoge los datos del formulario anterior para crear un nuevo profesor
     */
    @PostMapping("/add")
    public String addProfesor(@ModelAttribute("profesor") @NonNull Profesor profesor) {
        repoProfesor.save(profesor);
        return "redirect:/profesores";
    }

    /**
     * Muestra un formulario para confirmar el borrado del profesor
     */
    @GetMapping("/delete/{id}")
    public String deleteProfesorForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Profesor> oProfesor = repoProfesor.findById(id);
        if (oProfesor.isPresent())
            modelo.addAttribute("profesor", oProfesor.get());
        else {
            modelo.addAttribute("mensaje", "El profesor consultado no existe.");
            return "error";
        }
        return "profesores/delete";
    }

    /**
     * Elimina el profesor de la base de datos si es posible
     */
    @PostMapping("/delete/{id}")
    public String deleteProfesor(Model modelo, @PathVariable("id") @NonNull Long id) {
        try {
            repoProfesor.deleteById(id);
        } catch (Exception e) {
            modelo.addAttribute("mensaje", "El profesor no se puede eliminar porque tiene operaciones relacionadas.");
            return "error";
        }
        return "redirect:/profesores";
    }

    /**
     * Muestra un formulario para editar el profesor
     */
    @GetMapping("/edit/{id}")
    public String editProfesorForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Profesor> oProfesor = repoProfesor.findById(id);
        if (oProfesor.isPresent()) {
            modelo.addAttribute("profesor", oProfesor.get());
            return "profesores/edit";
        } else {
            modelo.addAttribute("mensaje", "El profesor consultado no existe.");
            return "error";
        }
    }

    /**
     * Guarda los cambios realizados en el formulario de edición del profesor
     */
    @PostMapping("/edit/{id}")
    public String editProfesor(Model modelo, @PathVariable("id") @NonNull Long id, @ModelAttribute("profesor") @NonNull Profesor profesor) {
        profesor.setId(id); // Aseguramos que el ID se establezca correctamente
        repoProfesor.save(profesor);
        return "redirect:/profesores";
    }

}
