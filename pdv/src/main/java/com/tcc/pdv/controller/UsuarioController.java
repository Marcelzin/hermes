package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.model.Usuario;
import com.tcc.pdv.repository.UsuarioRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.tcc.pdv.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/filtro")
    public ResponseEntity<List<Usuario>> filtrarUsuarios(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nivel_acesso,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Integer comercio_id = (Integer) session.getAttribute("comercioId");
        Integer user_id = (Integer) session.getAttribute("userId");

        List<Usuario> usuarios = usuarioRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Adiciona a condição para comercio_id
            predicates.add(criteriaBuilder.equal(root.get("comercio").get("id"), comercio_id));
            predicates.add(criteriaBuilder.notEqual(root.get("id"), user_id));

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            if (nivel_acesso != null && !nivel_acesso.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("nivelAcesso"), nivel_acesso));
            }
            if (nome != null && !nome.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("nome"), "%" + nome + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping
    public List<Usuario> getAllUsuarios(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer comercio_id = (Integer) session.getAttribute("comercioId");
        Integer user_id = (Integer) session.getAttribute("userId");
        return usuarioRepository.findByComercio_idAndIdNot(comercio_id, user_id);
    }

    @PostMapping
    public ResponseEntity<String> createUsuario(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        String email = (String) payload.get("email");
        Usuario usuarioExistente = usuarioRepository.findByEmail(email);
        
        // Tenta pegar o comercioId do payload, se não existir pega da sessão
        Integer comercioId = (Integer) payload.get("comercioId");
        if (comercioId == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                comercioId = (Integer) session.getAttribute("comercioId");
            }
        }
        
        Comercio comercio = comercioRepository.findById(comercioId).orElse(null);

        if (comercio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de Comércio não cadastrado.");
        }

        if (usuarioExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail " + email + " já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome((String) payload.get("nome"));
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode((String) payload.get("senha")));
        usuario.setNivelAcesso((String) payload.get("nivelAcesso"));
        usuario.setStatus((String) payload.get("status"));
        usuario.setComercio(comercio);

        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable int id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setStatus("INATIVO");
                    usuarioRepository.save(usuario);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> verificarLoginUsuario(@RequestBody Map<String, Object> payload,
            HttpServletRequest request) {
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
        } else if (!"ATIVO".equals(usuario.getStatus())) {
            response.put("error", "Usuário inativo.");
            return ResponseEntity.status(403).body(response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("userId", usuario.getId());
            session.setAttribute("comercioId", usuario.getComercio().getId());
            response.put("message", "Login realizado com sucesso!");
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails,
            HttpServletRequest request) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNome(usuarioDetails.getNome());
            usuario.setEmail(usuarioDetails.getEmail());
            
            // Verifica se a senha foi enviada e não está vazia
            String novaSenha = usuarioDetails.getSenha();
            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(novaSenha));
            }
            
            usuario.setNivelAcesso(usuarioDetails.getNivelAcesso());
            usuario.setStatus(usuarioDetails.getStatus());

            // Obter o ID do comércio da sessão do usuário atual
            HttpSession session = request.getSession();
            Integer comercioId = (Integer) session.getAttribute("comercioId");
            Comercio comercio = comercioRepository.findById(comercioId).orElse(null);
            if (comercio != null) {
                usuario.setComercio(comercio);
            }

            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return ResponseEntity.ok(updatedUsuario);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}