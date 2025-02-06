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
}