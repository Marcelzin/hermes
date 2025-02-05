package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.model.Usuario;
import com.tcc.pdv.repository.UsuarioRepository;
import com.tcc.pdv.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Map<String, Object> payload) {
        int comercioId = (int) payload.get("comercioId");
        Comercio comercio = comercioRepository.findById(comercioId).orElse(null);
        if (comercio != null) {
            Usuario usuario = new Usuario();
            usuario.setNome((String) payload.get("nome"));
            usuario.setEmail((String) payload.get("email"));
            usuario.setSenha(passwordEncoder.encode((String) payload.get("senha")));
            usuario.setNivelAcesso((String) payload.get("nivelAcesso"));
            usuario.setStatus((String) payload.get("status"));
            usuario.setComercio(comercio);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNome(usuarioDetails.getNome());
            usuario.setEmail(usuarioDetails.getEmail());
            usuario.setSenha(passwordEncoder.encode(usuarioDetails.getSenha()));
            usuario.setNivelAcesso(usuarioDetails.getNivelAcesso());
            usuario.setStatus(usuarioDetails.getStatus());
            Comercio comercio = comercioRepository.findById(usuarioDetails.getComercio().getId()).orElse(null);
            if (comercio != null) {
                usuario.setComercio(comercio);
            }
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        usuarioRepository.deleteById(id);
    }
}