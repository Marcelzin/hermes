package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    @Autowired
    private ComercioRepository comercioRepository;

    @GetMapping
    public List<Comercio> getAllComercios() {
        return comercioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createComercio(@RequestBody Comercio comercio) {
        Comercio savedComercio = comercioRepository.save(comercio);
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedComercio.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Comercio getComercioById(@PathVariable int id) {
        return comercioRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Comercio updateComercio(@PathVariable int id, @RequestBody Comercio comercioDetails) {
        Comercio comercio = comercioRepository.findById(id).orElse(null);
        if (comercio != null) {
            comercio.setNome(comercioDetails.getNome());
            comercio.setCpfCnpj(comercioDetails.getCpfCnpj());
            return comercioRepository.save(comercio);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteComercio(@PathVariable int id) {
        comercioRepository.deleteById(id);
    }
}