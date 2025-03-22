let carrinho = [];

function finalizarVenda() {
    if (carrinho.length === 0) {
        alert('Adicione produtos ao carrinho antes de finalizar a venda!');
        return;
    }

    // Mostrar modal de forma de pagamento
    $('#formaPagamentoModal').modal('show');
}

function confirmarVenda(formaPagamentoId) {
    const venda = {
        formaPagamento: formaPagamentoId,
        valorTotal: parseFloat($('#total').text().replace('R$ ', '').replace(',', '.')),
        itens: carrinho.map(item => ({
            produtoId: item.produto.id,
            quantidade: parseInt(item.quantidade),
            valorUnitario: parseFloat(item.produto.valorVenda.toFixed(2))
        }))
    };

    $.ajax({
        url: '/api/vendas',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(venda),
        success: function(response) {
            $('#formaPagamentoModal').modal('hide');
            Swal.fire({
                icon: 'success',
                title: 'Sucesso!',
                text: 'Venda finalizada com sucesso!'
            }).then(() => {
                carrinho = [];
                atualizarInterface();
            });
        },
        error: function(xhr) {
            $('#formaPagamentoModal').modal('hide');
            Swal.fire({
                icon: 'error',
                title: 'Erro!',
                text: 'Erro ao finalizar venda: ' + (xhr.responseText || 'Erro desconhecido')
            });
        }
    });
}

$(document).ready(function() {
    // Handler para o botão de adicionar
    $('#btnAdicionarProduto').click(function() {
        buscarEAdicionarProduto();
    });

    // Handler para a tecla Enter no input
    $('#codigoBarras').on('keypress', function(e) {
        if (e.which === 13) { // Código da tecla Enter
            e.preventDefault(); // Previne o comportamento padrão do Enter
            buscarEAdicionarProduto();
        }
    });

    // Carregar formas de pagamento
    carregarFormasPagamento();
});

function buscarEAdicionarProduto() {
    const codigoBarras = $('#codigoBarras').val().trim();
    
    if (!codigoBarras) {
        Swal.fire({
            icon: 'warning',
            title: 'Atenção',
            text: 'Por favor, insira um código de barras!'
        });
        return;
    }

    $.ajax({
        url: '/api/produtos/barra/' + codigoBarras,
        type: 'GET',
        success: function(produto) {
            if (produto) {
                adicionarProdutoAoCarrinho(produto);
                $('#codigoBarras').val('').focus(); // Limpa o input e mantém o foco
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Produto não encontrado!'
                });
            }
        },
        error: function() {
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao buscar produto!'
            });
        }
    });
}

function adicionarProdutoAoCarrinho(produto) {
    const itemExistente = carrinho.find(item => item.produto.id === produto.id);
    
    if (itemExistente) {
        itemExistente.quantidade++;
    } else {
        carrinho.push({
            produto: produto,
            quantidade: 1
        });
    }
    
    atualizarInterface();
    
    // Feedback visual
    Swal.fire({
        icon: 'success',
        title: 'Produto adicionado!',
        text: produto.nome,
        timer: 1000,
        showConfirmButton: false
    });
}

function atualizarInterface() {
    const listaProdutos = $('#listaProdutos');
    listaProdutos.empty();
    
    let subtotal = 0;
    
    carrinho.forEach(item => {
        const valorTotal = item.produto.valorVenda * item.quantidade;
        subtotal += valorTotal;
        
        listaProdutos.append(`
            <div class="produto-item mb-3">
                <div class="card">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-8">
                                <h5 class="card-title">${item.produto.nome}</h5>
                                <p class="card-text text-muted small">${item.produto.descricao || 'Sem descrição'}</p>
                            </div>
                            <div class="col-md-4 text-right">
                                <button class="btn btn-sm btn-danger" onclick="removerProduto(${item.produto.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                        <div class="row mt-2">
                            <div class="col-md-4">
                                <small class="text-muted">Valor unitário:</small>
                                <div>R$ ${item.produto.valorVenda.toFixed(2)}</div>
                            </div>
                            <div class="col-md-4">
                                <small class="text-muted">Quantidade:</small>
                                <div class="input-group input-group-sm">
                                    <input type="number" 
                                           class="form-control" 
                                           value="${item.quantidade}" 
                                           min="1"
                                           onchange="atualizarQuantidade(${item.produto.id}, this.value)">
                                </div>
                            </div>
                            <div class="col-md-4 text-right">
                                <small class="text-muted">Total:</small>
                                <div><strong>R$ ${valorTotal.toFixed(2)}</strong></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `);
    });
    
    $('#subtotal').text(`R$ ${subtotal.toFixed(2)}`);
    $('#total').text(`R$ ${subtotal.toFixed(2)}`);
}

// Carregar formas de pagamento ao iniciar
$(document).ready(function() {
    // ... código anterior ...

    // Carregar formas de pagamento
    $.ajax({
        url: '/api/vendas/formas-pagamento',
        type: 'GET',
        success: function(formasPagamento) {
            const container = $('#formasPagamentoContainer');
            formasPagamento.forEach(forma => {
                container.append(`
                    <button class="btn btn-lg btn-outline-primary m-2" 
                            onclick="confirmarVenda(${forma.id})">
                        ${forma.tipo}
                    </button>
                `);
            });
        }
    });
});

function removerProduto(produtoId) {
    Swal.fire({
        title: 'Confirmar remoção',
        text: "Deseja remover este item do carrinho?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sim, remover!',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            carrinho = carrinho.filter(item => item.produto.id !== produtoId);
            atualizarInterface();
            
            Swal.fire({
                icon: 'success',
                title: 'Removido!',
                text: 'Item removido do carrinho.',
                timer: 1000,
                showConfirmButton: false
            });
        }
    });
}

function atualizarQuantidade(produtoId, novaQuantidade) {
    const item = carrinho.find(item => item.produto.id === produtoId);
    if (item) {
        const quantidade = parseInt(novaQuantidade);
        
        if (quantidade <= 0) {
            removerProduto(produtoId);
            return;
        }
        
        if (quantidade >= 1000) {
            Swal.fire({
                icon: 'warning',
                title: 'Atenção',
                text: 'Quantidade máxima permitida é 999 unidades.'
            });
            item.quantidade = 999;
        } else {
            item.quantidade = quantidade;
        }
        
        atualizarInterface();
    }
} 