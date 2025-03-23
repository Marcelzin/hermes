package com.tcc.pdv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tcc.pdv.model.Produto;
import com.tcc.pdv.repository.ProdutoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class Routes {

    @Autowired
    public ProdutoRepository produtoRepository;

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

    @GetMapping("/produtos")
    public String showProdutos(Model model) {
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);
        return "produtos";
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

    @GetMapping("/formas-pagamento")
    public String showFormasPagamento() {
        return "formas-pagamento";
    }
}