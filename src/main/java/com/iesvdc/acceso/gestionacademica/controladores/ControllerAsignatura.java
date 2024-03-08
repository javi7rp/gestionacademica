package com.iesvdc.acceso.gestionacademica.controladores;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.iesvdc.acceso.gestionacademica.modelos.Asignatura;
import com.iesvdc.acceso.gestionacademica.repos.RepoAsignatura;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/asignatura")
public class ControllerAsignatura {
    @Autowired
    private RepoAsignatura repoAsignatura;

    //Lista Asignaturas

    @GetMapping
    public String listarAsignaturas(Model modelo){
        modelo.addAttribute("asignaturas", repoAsignatura.findAll());
        return "asignatura/asignaturas";
    }

    //Añadir Asignatura

    @GetMapping("/add")
    public String addAsignaturaGet(Model modelo) {
        modelo.addAttribute("asignatura", new Asignatura());
        return "asignatura/add";
    }

    @PostMapping("/add")
    public String addAsignatura(@ModelAttribute("asignatura") @NonNull Asignatura asignatura) {
        repoAsignatura.save(asignatura);
        return "redirect:/asignatura";
    }

    //Editar Asignatura

    @GetMapping("/edit/{id}")
    public String editAsignaturaForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional <Asignatura> objAsignatura = repoAsignatura.findById(id);
        if(objAsignatura.isPresent()) {
            modelo.addAttribute(
            "asignatura", objAsignatura.get());
            return "asignatura/edit";
        } else {
            modelo.addAttribute("mensaje", "La asignatura consultada no existe.");
            return "error";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editAsignatura(@PathVariable("id") @NonNull Long id, @ModelAttribute("asignaturaModificada") Asignatura asignaturaModificada) {
        Optional <Asignatura> objAsignatura = repoAsignatura.findById(id);

        if(objAsignatura.isPresent()){
            Asignatura asignaturaExistente = objAsignatura.get();

            asignaturaExistente.setNombre(asignaturaModificada.getNombre());
            asignaturaExistente.setCurso(asignaturaModificada.getCurso());
            asignaturaExistente.setCiclo(asignaturaModificada.getCiclo());

            repoAsignatura.save(asignaturaExistente);

            return "redirect:/asignatura";
        }else{
            return "error";
        }
    }

    //Eliminar Asignatura

    @GetMapping("/delete/{id}")
    public String deleteAsignaturaForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional <Asignatura> objAsignatura = repoAsignatura.findById(id);
        if (objAsignatura.isPresent()){
            modelo.addAttribute(
            "asignatura", objAsignatura.get());
            return "asignatura/delete";
        } else {
            modelo.addAttribute(
                "mensaje", "La asignatura consultada no existe.");
            return "error";
        }
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteAsignatura(@PathVariable("id") @NonNull Long id) {
        repoAsignatura.deleteById(id);
        return "redirect:/asignatura";
    }
}
