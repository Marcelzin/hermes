package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.model.FormaPagamento;
import com.tcc.pdv.model.Usuario;
import com.tcc.pdv.repository.ComercioRepository;
import com.tcc.pdv.repository.FormaPagamentoRepository;
import com.tcc.pdv.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formas-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private boolean verificarAcessoAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return false;

        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        return usuario != null && "prop".equals(usuario.getNivelAcesso());
    }

    @GetMapping
    public ResponseEntity<List<FormaPagamento>> getAllFormasPagamento(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer comercioId = (Integer) session.getAttribute("comercioId");
        
        List<FormaPagamento> formasPagamento = formaPagamentoRepository.findByComercioIdAndStatus(comercioId, "ATIVO");
        return ResponseEntity.ok(formasPagamento);
    }

    @PostMapping
    public ResponseEntity<?> createFormaPagamento(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        if (!verificarAcessoAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado. Apenas administradores podem criar formas de pagamento.");
        }

        HttpSession session = request.getSession(false);
        Integer comercioId = (Integer) session.getAttribute("comercioId");
        Comercio comercio = comercioRepository.findById(comercioId).orElse(null);

        if (comercio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comércio não encontrado.");
        }

        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setTipo(payload.get("tipo"));
        formaPagamento.setStatus("ATIVO");
        formaPagamento.setComercio(comercio);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(formaPagamentoRepository.save(formaPagamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFormaPagamento(@PathVariable int id, @RequestBody Map<String, String> payload, HttpServletRequest request) {
        if (!verificarAcessoAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado. Apenas administradores podem editar formas de pagamento.");
        }

        return formaPagamentoRepository.findById(id)
                .map(formaPagamento -> {
                    formaPagamento.setTipo(payload.get("tipo"));
                    return ResponseEntity.ok(formaPagamentoRepository.save(formaPagamento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFormaPagamento(@PathVariable int id, HttpServletRequest request) {
        if (!verificarAcessoAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado. Apenas administradores podem desativar formas de pagamento.");
        }

        return formaPagamentoRepository.findById(id)
                .map(formaPagamento -> {
                    formaPagamento.setStatus("INATIVO");
                    formaPagamentoRepository.save(formaPagamento);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 