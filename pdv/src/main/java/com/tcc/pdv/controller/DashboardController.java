package com.tcc.pdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tcc.pdv.model.*;
import com.tcc.pdv.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> getDashboardData(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            Integer comercioId = (Integer) session.getAttribute("comercioId");

            Map<String, Object> dashboardData = new HashMap<>();
            LocalDate hoje = LocalDate.now();

            // Dados de Vendas
            List<Pedido> pedidos = pedidoRepository.findByComercioId(comercioId);
            long vendasHoje = pedidos.stream()
                .filter(p -> p.getDataPedido().toLocalDate().equals(hoje))
                .count();
            
            double faturamentoHoje = pedidos.stream()
                .filter(p -> p.getDataPedido().toLocalDate().equals(hoje))
                .mapToDouble(Pedido::getValorTotal)
                .sum();

            double lucroHoje = pedidos.stream()
                .filter(p -> p.getDataPedido().toLocalDate().equals(hoje))
                .mapToDouble(Pedido::getLucroObtido)
                .sum();

            // Dados do mês atual
            LocalDate inicioMes = hoje.withDayOfMonth(1);
            long vendasMes = pedidos.stream()
                .filter(p -> !p.getDataPedido().toLocalDate().isBefore(inicioMes))
                .count();

            double faturamentoMes = pedidos.stream()
                .filter(p -> !p.getDataPedido().toLocalDate().isBefore(inicioMes))
                .mapToDouble(Pedido::getValorTotal)
                .sum();

            double lucroMes = pedidos.stream()
                .filter(p -> !p.getDataPedido().toLocalDate().isBefore(inicioMes))
                .mapToDouble(Pedido::getLucroObtido)
                .sum();

            // Adicionar dados de vendas
            dashboardData.put("vendasHoje", vendasHoje);
            dashboardData.put("vendasMes", vendasMes);
            dashboardData.put("faturamentoHoje", faturamentoHoje);
            dashboardData.put("faturamentoMes", faturamentoMes);
            dashboardData.put("lucroHoje", lucroHoje);
            dashboardData.put("lucroMes", lucroMes);

            // Dados de Produtos
            List<Produto> produtos = produtoRepository.findByComercioId(comercioId);
            dashboardData.put("totalProdutos", produtos.size());
            dashboardData.put("produtosAtivos", produtos.stream()
                .filter(p -> "ATIVO".equals(p.getStatus()))
                .count());
            dashboardData.put("produtosInativos", produtos.stream()
                .filter(p -> "INATIVO".equals(p.getStatus()))
                .count());
            dashboardData.put("produtosBaixoEstoque", produtos.stream()
                .filter(p -> "ATIVO".equals(p.getStatus()) && p.getQuantidade() < 10)
                .count());

            // Dados de Usuários
            List<Usuario> usuarios = usuarioRepository.findByComercioId(comercioId);
            dashboardData.put("totalUsuarios", usuarios.size());
            dashboardData.put("usuariosAtivos", usuarios.stream()
                .filter(u -> "ATIVO".equals(u.getStatus()))
                .count());
            dashboardData.put("usuariosInativos", usuarios.stream()
                .filter(u -> "INATIVO".equals(u.getStatus()))
                .count());

            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Erro ao carregar dados do dashboard"));
        }
    }
} 