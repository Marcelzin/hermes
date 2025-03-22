package com.tcc.pdv.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcc.pdv.model.*;
import com.tcc.pdv.repository.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/vendas")
public class VendasController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            Integer comercioId = (Integer) session.getAttribute("comercioId");
            Integer userId = (Integer) session.getAttribute("userId");

            Comercio comercio = comercioRepository.findById(comercioId).orElse(null);
            Usuario responsavel = usuarioRepository.findById(userId).orElse(null);
            
            // Converter formaPagamento para Integer de forma segura
            Integer formaPagamentoId = null;
            if (payload.get("formaPagamento") instanceof Integer) {
                formaPagamentoId = (Integer) payload.get("formaPagamento");
            } else if (payload.get("formaPagamento") instanceof String) {
                formaPagamentoId = Integer.parseInt((String) payload.get("formaPagamento"));
            }
            
            FormaPagamento pagamento = formaPagamentoRepository.findById(formaPagamentoId).orElse(null);

            if (comercio == null || responsavel == null || pagamento == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
            }

            // Criar o pedido
            Pedido pedido = new Pedido();
            pedido.setDataPedido(Date.valueOf(LocalDate.now()));
            
            // Converter valorTotal para Double de forma segura
            Double valorTotal = null;
            Object valorTotalObj = payload.get("valorTotal");
            if (valorTotalObj instanceof Double) {
                valorTotal = (Double) valorTotalObj;
            } else if (valorTotalObj instanceof Integer) {
                valorTotal = ((Integer) valorTotalObj).doubleValue();
            } else if (valorTotalObj instanceof String) {
                valorTotal = Double.parseDouble((String) valorTotalObj);
            }
            
            pedido.setValorTotal(valorTotal);
            pedido.setResponsavel(responsavel);
            pedido.setPagamento(pagamento);
            pedido.setComercio(comercio);

            double lucroTotal = 0.0;

            // Salvar o pedido
            Pedido pedidoSalvo = pedidoRepository.save(pedido);

            // Processar os itens do pedido
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itens = (List<Map<String, Object>>) payload.get("itens");
            
            for (Map<String, Object> item : itens) {
                // Converter valores de forma segura
                Integer produtoId = (Integer) item.get("produtoId");
                Integer quantidade = (Integer) item.get("quantidade");
                
                Double valorUnitario = null;
                Object valorUnitarioObj = item.get("valorUnitario");
                if (valorUnitarioObj instanceof Double) {
                    valorUnitario = (Double) valorUnitarioObj;
                } else if (valorUnitarioObj instanceof Integer) {
                    valorUnitario = ((Integer) valorUnitarioObj).doubleValue();
                }

                Produto produto = produtoRepository.findById(produtoId).orElse(null);
                if (produto != null) {
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setPedido(pedidoSalvo);
                    itemPedido.setProduto(produto);
                    itemPedido.setQuantidade(quantidade);
                    itemPedido.setValorUnitario(valorUnitario);
                    itemPedido.setValorTotal(quantidade * valorUnitario);

                    // Calcular lucro do item
                    double lucroItem = (valorUnitario - produto.getValorFabrica()) * quantidade;
                    lucroTotal += lucroItem;

                    itemPedidoRepository.save(itemPedido);
                }
            }

            // Atualizar o lucro total do pedido
            pedidoSalvo.setLucroObtido(lucroTotal);
            pedidoRepository.save(pedidoSalvo);

            return ResponseEntity.status(HttpStatus.CREATED).body("Pedido criado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace(); // Para log detalhado no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar pedido: " + e.getMessage());
        }
    }

    @GetMapping("/formas-pagamento")
    public ResponseEntity<List<FormaPagamento>> getFormasPagamento() {
        return ResponseEntity.ok(formaPagamentoRepository.findAll());
    }
}
