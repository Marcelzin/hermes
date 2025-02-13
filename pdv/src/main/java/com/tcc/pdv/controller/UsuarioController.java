package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.model.Usuario;
import com.tcc.pdv.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.tcc.pdv.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<Usuario> createUsuario(@RequestBody Map<String, Object> payload) {
        int comercioId = (int) payload.get("comercioId");
        String email = (String) payload.get("email");
        Usuario usuarioExistente = usuarioRepository.findByEmail(email);
        Comercio comercio = comercioRepository.findById(comercioId).orElse(null);

        if (comercio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (usuarioExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Usuario usuario = new Usuario();
        usuario.setNome((String) payload.get("nome"));
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode((String) payload.get("senha")));
        usuario.setNivelAcesso((String) payload.get("nivelAcesso"));
        usuario.setStatus((String) payload.get("status"));
        usuario.setComercio(comercio);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    // Método para buscar um usuário pelo ID
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // Método para login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> verificarLoginUsuario(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        String email = (String) payload.get("email");
        String senha = (String) payload.get("senha");

        Usuario usuario = usuarioRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();
        if (usuario == null) {
            response.put("error", "E-mail não cadastrado.");
            return ResponseEntity.status(404).body(response);
        } else if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            response.put("error", "Senha incorreta.");
            return ResponseEntity.status(401).body(response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("userId", usuario.getId());
            session.setAttribute("comercioId", usuario.getComercio().getId());
            response.put("message", "Login realizado com sucesso!");
            return ResponseEntity.ok(response);
        }
    }

    // Método para atualizar um usuário
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

    // Método para deletar um usuário
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        usuarioRepository.deleteById(id);
    }
}