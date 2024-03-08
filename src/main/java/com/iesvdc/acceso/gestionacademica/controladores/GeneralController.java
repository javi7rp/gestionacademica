package com.iesvdc.acceso.gestionacademica.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping("/ayuda")
    public String showAyuda() {
        return "ayuda";
    }

    @GetMapping("/acerca")
    public String showAcerca() {
        return "acerca";
    }

    @GetMapping("/error")
    public String showError() {
        return "error";
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    // @GetMapping("/login")
    // public String showlogin() {
    //     return "login";
    // }

    @GetMapping("/denegado")
    public String showlogin() {
        return "denegado";
    }
}
