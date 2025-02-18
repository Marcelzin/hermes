package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.model.Produto;
import com.tcc.pdv.repository.ComercioRepository;
import com.tcc.pdv.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Map<String, Object> payload) {
        Integer comercioId = (Integer) payload.get("comercioId");
        String nome = (String) payload.get("nome");

        if (comercioId == null || nome == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Comercio comercio = comercioRepository.findById(comercioId).orElse(null);

        if (comercio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setValorFabrica((Double) payload.get("valorFabrica"));
        produto.setValorVenda((Double) payload.get("valorVenda"));
        produto.setDescricao((String) payload.get("descricao"));
        produto.setImagem((String) payload.get("imagem"));
        produto.setStatus((String) payload.get("status"));
        produto.setComercio(comercio);

        Produto savedProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable int id) {
        return produtoRepository.findById(id).orElse(null);
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
            Comercio comercio = comercioRepository.findById(produtoDetails.getComercio().getId()).orElse(null);
            if (comercio != null) {
                produto.setComercio(comercio);
            }
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