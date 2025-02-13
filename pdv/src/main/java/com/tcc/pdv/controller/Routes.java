package com.tcc.pdv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}