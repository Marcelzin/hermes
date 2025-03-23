$(document).ready(function() {
    carregarDados();
    setInterval(carregarDados, 60000); // Atualiza a cada minuto
});

function carregarDados() {
    $.ajax({
        url: '/api/dashboard/resumo',
        type: 'GET',
        success: function(data) {
            atualizarDashboardVendas(data);
            atualizarDashboardProdutos(data);
            atualizarDashboardUsuarios(data);
        },
        error: function(xhr) {
            console.error('Erro ao carregar dados do dashboard:', xhr);
        }
    });
}

function formatarMoeda(valor) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor);
}

function atualizarDashboardVendas(data) {
    $('#vendasHoje').text(data.vendasHoje);
    $('#faturamentoHoje').text(formatarMoeda(data.faturamentoHoje));
    $('#lucroHoje').text(formatarMoeda(data.lucroHoje));
    $('#vendasMes').text(data.vendasMes);

    // Atualizar gráfico de vendas
    const ctx = document.getElementById('vendasChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
            datasets: [{
                label: 'Vendas',
                data: [data.vendasMes],
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

function atualizarDashboardProdutos(data) {
    $('#totalProdutos').text(data.totalProdutos);
    $('#produtosAtivos').text(data.produtosAtivos);
    $('#produtosBaixoEstoque').text(data.produtosBaixoEstoque);
    $('#produtosInativos').text(data.produtosInativos);

    // Atualizar gráfico de produtos
    const ctx = document.getElementById('produtosChart').getContext('2d');
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Ativos', 'Baixo Estoque', 'Inativos'],
            datasets: [{
                data: [
                    data.produtosAtivos,
                    data.produtosBaixoEstoque,
                    data.produtosInativos
                ],
                backgroundColor: [
                    'rgb(75, 192, 192)',
                    'rgb(255, 205, 86)',
                    'rgb(255, 99, 132)'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

function atualizarDashboardUsuarios(data) {
    $('#totalUsuarios').text(data.totalUsuarios);
    $('#usuariosAtivos').text(data.usuariosAtivos);
    $('#usuariosInativos').text(data.usuariosInativos);
}

function logoff() {
    window.location.href = "/logout";
}

function editarUsuario() {
    window.location.href = "/editar-usuario";
} 