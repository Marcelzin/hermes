package com.tcc.pdv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Routes {

    @GetMapping("")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/cadastro")
    public String showCadastro() {
        return "cadastro";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/home")
    public String showHome() {
        return "home";
    }

    @GetMapping("/vendas")
    public String showVendas() {
        return "vendas";
    }

    @GetMapping("/equipe")
    public String showEquipe() {
        return "equipe";
    }
}