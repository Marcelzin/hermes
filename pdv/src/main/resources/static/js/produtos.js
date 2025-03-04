function logoff() {
    window.location.href = "/logout";
}

function editarUsuario() {
    window.location.href = "/editar-usuario";
}

$(document).ready(function () {
    $('#valorFabrica, #valorVenda').maskMoney({
        prefix: 'R$ ',
        allowNegative: false,
        thousands: '.',
        decimal: ',',
        affixesStay: false,
        precision: 2
    });

    var table = $('#tabelaProdutos').DataTable();

    $('#filtroForm input, #filtroForm select').on('keyup change', function () {
        table.draw();
    });
});

function novoProduto() {
    document.getElementById('produtoForm').reset();
    $('#valorFabrica, #valorVenda').maskMoney('mask', 0.00);
    $('#produtoModalLabel').text('Novo Produto');
    $('#salvarProdutoBtn').show();
    $('#atualizarProdutoBtn').hide();
    $('#produtoModal').modal('show');
}

function salvarProduto() {
    const valorFabrica = parseFloat($('#valorFabrica').maskMoney('unmasked')[0]).toFixed(2);
    const valorVenda = parseFloat($('#valorVenda').maskMoney('unmasked')[0]).toFixed(2);

    const produto = {
        barra: parseInt(document.getElementById('barra').value),
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        valorFabrica: parseFloat(valorFabrica),
        valorVenda: parseFloat(valorVenda),
        imagem: document.getElementById('imagem').value,
        status: document.getElementById('status').value,
    };

    $.ajax({
        url: '/api/produtos',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(produto),
        success: function (response) {
            $('#produtoModal').modal('hide');
            location.reload();
        },
        error: function (xhr) {
            alert('Erro ao salvar produto: ' + xhr.responseText);
        }
    });
}

function editarProduto(id) {
    $.ajax({
        url: '/api/produtos/' + id,
        type: 'GET',
        success: function (produto) {
            $('#produtoId').val(produto.id);
            $('#barra').val(produto.barra);
            $('#nome').val(produto.nome);
            $('#descricao').val(produto.descricao);
            $('#valorFabrica').maskMoney('mask', produto.valorFabrica);
            $('#valorVenda').maskMoney('mask', produto.valorVenda);
            $('#imagem').val(produto.imagem);
            $('#status').val(produto.status);
            $('#produtoModalLabel').text('Editar Produto');
            $('#salvarProdutoBtn').hide();
            $('#atualizarProdutoBtn').show();
            $('#produtoModal').modal('show');
        },
        error: function (xhr) {
            alert('Erro ao carregar produto: ' + xhr.responseText);
        }
    });
}

function atualizarProduto() {
    const id = $('#produtoId').val();
    const valorFabrica = $('#valorFabrica').maskMoney('unmasked')[0];
    const valorVenda = $('#valorVenda').maskMoney('unmasked')[0];

    const produto = {
        barra: parseInt(document.getElementById('barra').value),
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        valorFabrica: valorFabrica,
        valorVenda: valorVenda,
        imagem: document.getElementById('imagem').value,
        status: document.getElementById('status').value,
    };

    // Validação dos campos obrigatórios
    if (!produto.barra || !produto.nome || !produto.valorVenda || !produto.status) {
        alert('Por favor, preencha todos os campos obrigatórios: Barra, Nome, Valor de Venda e Status.');
        return;
    }

    $.ajax({
        url: '/api/produtos/' + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(produto),
        success: function (response) {
            $('#produtoModal').modal('hide');
            location.reload();
        },
        error: function (xhr) {
            alert('Erro ao atualizar produto: ' + xhr.responseText);
        }
    });
}

function desativarProduto(id) {
    if (confirm('Tem certeza que deseja desativar este produto?')) {
        $.ajax({
            url: '/api/produtos/' + id,
            type: 'DELETE',
            success: function () {
                location.reload();
            },
            error: function (xhr) {
                alert('Erro ao desativar produto: ' + xhr.responseText);
            }
        });
    }
}

function filtrarProdutos() {
    const barra = $('#filtroBarra').val();
    const nome = $('#filtroNome').val();
    const descricao = $('#filtroDescricao').val();
    const imagem = $('#filtroImagem').val();
    const status = $('#filtroStatus').val();
    const valorFabrica = $('#filtroValorFabrica').val();
    const valorVenda = $('#filtroValorVenda').val();

    $.ajax({
        url: '/api/produtos/filtro',
        type: 'GET',
        data: {
            barra: barra,
            nome: nome,
            descricao: descricao,
            imagem: imagem,
            status: status,
            valorFabrica: valorFabrica,
            valorVenda: valorVenda
        },
        success: function (data) {
            var table = $('#tabelaProdutos').DataTable();
            table.clear().rows.add(data.map(produto => [
                produto.barra,
                produto.nome,
                produto.valorFabrica,
                produto.valorVenda,
                produto.descricao,
                produto.status,
                `<div class="d-flex gap-2 justify-content-center">
                            <button class="btn btn-warning btn-sm" onclick="editarProduto(${produto.id})">
                                <i class="fas fa-edit"></i> Editar
                            </button>
                            <button class="btn btn-danger btn-sm" onclick="desativarProduto(${produto.id})">
                                <i class="fas fa-ban"></i> Desabilitar
                            </button>
                        </div>`
            ])).draw();
        },
        error: function (xhr) {
            alert('Erro ao filtrar produtos: ' + xhr.responseText);
        }
    });
}

function limparFiltros() {
    $('#filtroForm')[0].reset();
    var table = $('#tabelaProdutos').DataTable();
    table.search('').columns().search('').draw();
}