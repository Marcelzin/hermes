package com.tcc.pdv.controller;

import com.tcc.pdv.model.Comercio;
import com.tcc.pdv.model.FormaPagamento;
import com.tcc.pdv.model.Pedido;
import com.tcc.pdv.model.Usuario;
import com.tcc.pdv.repository.ComercioRepository;
import com.tcc.pdv.repository.FormaPagamentoRepository;
import com.tcc.pdv.repository.PedidoRepository;
import com.tcc.pdv.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer comercioId = (Integer) session.getAttribute("comercioId");
        Integer userId = (Integer) session.getAttribute("userId");

        Comercio comercio = comercioRepository.findById(comercioId).orElse(null);
        Usuario responsavel = usuarioRepository.findById(userId).orElse(null);
        FormaPagamento pagamento = formaPagamentoRepository.findById((Integer) payload.get("formaPagamento")).orElse(null);

        if (comercio == null || responsavel == null || pagamento == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        Pedido pedido = new Pedido();
        pedido.setDataPedido(Date.valueOf(LocalDate.now()));
        pedido.setValorTotal((Double) payload.get("total"));
        pedido.setLucroObtido(0); // Calcular lucro obtido conforme necessário
        pedido.setResponsavel(responsavel);
        pedido.setPagamento(pagamento);
        pedido.setComercio(comercio);

        pedidoRepository.save(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body("Pedido criado com sucesso.");
    }

    @GetMapping
    public List<Pedido> listarPedidos(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer comercioId = (Integer) session.getAttribute("comercioId");

        return pedidoRepository.findByComercioId(comercioId);
    }

}