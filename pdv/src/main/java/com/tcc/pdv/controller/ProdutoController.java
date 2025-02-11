package com.tcc.pdv.controller;

import com.tcc.pdv.model.Produto;
import com.tcc.pdv.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public String listarProdutos(Model model) {
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);
        return "produtos";
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.ok(novoProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable int id, @RequestBody Produto produtoDetails) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto != null) {
            produto.setNome(produtoDetails.getNome());
            produto.setValorFabrica(produtoDetails.getValorFabrica());
            produto.setValorVenda(produtoDetails.getValorVenda());
            produto.setDescricao(produtoDetails.getDescricao());
            produto.setImagem(produtoDetails.getImagem());
            produto.setStatus(produtoDetails.getStatus());
            Produto produtoAtualizado = produtoRepository.save(produto);
            return ResponseEntity.ok(produtoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarProduto(@PathVariable int id) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto != null) {
            produto.setStatus("INATIVO");
            produtoRepository.save(produto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}