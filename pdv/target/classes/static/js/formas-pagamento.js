$(document).ready(function() {
    $('#tabelaFormasPagamento').DataTable({
        language: {
            url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/pt-BR.json'
        },
        order: [[0, 'desc']],
        ajax: {
            url: '/api/formas-pagamento',
            dataSrc: ''
        },
        columns: [
            { data: 'id' },
            { data: 'tipo' },
            { data: 'status' },
            {
                data: null,
                render: function(data, type, row) {
                    return `
                        <div class="d-flex gap-2 justify-content-center">
                            <button class="btn btn-warning btn-sm" onclick="editarFormaPagamento(${row.id})">
                                <i class="fas fa-edit"></i> Editar
                            </button>
                            <button class="btn btn-danger btn-sm" onclick="desativarFormaPagamento(${row.id})">
                                <i class="fas fa-ban"></i> Desabilitar
                            </button>
                        </div>
                    `;
                }
            }
        ]
    });
});

function novaFormaPagamento() {
    document.getElementById('formaPagamentoForm').reset();
    $('#formaPagamentoModalLabel').text('Nova Forma de Pagamento');
    $('#salvarFormaPagamentoBtn').show();
    $('#atualizarFormaPagamentoBtn').hide();
    $('#formaPagamentoModal').modal('show');
}

function salvarFormaPagamento() {
    const tipo = $('#tipo').val().trim();
    const status = $('#status').val();
    
    if (!tipo) {
        Swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Por favor, preencha o tipo de pagamento.'
        });
        return;
    }

    $.ajax({
        url: '/api/formas-pagamento',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ 
            tipo: tipo,
            status: status 
        }),
        success: function() {
            $('#formaPagamentoModal').modal('hide');
            $('#tabelaFormasPagamento').DataTable().ajax.reload();
            Swal.fire({
                icon: 'success',
                title: 'Sucesso',
                text: 'Forma de pagamento cadastrada com sucesso!'
            });
        },
        error: function(xhr) {
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: xhr.responseText || 'Erro ao cadastrar forma de pagamento.'
            });
        }
    });
}

function editarFormaPagamento(id) {
    $.ajax({
        url: `/api/formas-pagamento/${id}`,
        type: 'GET',
        success: function(formaPagamento) {
            $('#formaPagamentoId').val(formaPagamento.id);
            $('#tipo').val(formaPagamento.tipo);
            $('#status').val(formaPagamento.status);
            $('#formaPagamentoModalLabel').text('Editar Forma de Pagamento');
            $('#salvarFormaPagamentoBtn').hide();
            $('#atualizarFormaPagamentoBtn').show();
            $('#formaPagamentoModal').modal('show');
        },
        error: function() {
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao carregar dados da forma de pagamento.'
            });
        }
    });
}

function atualizarFormaPagamento() {
    const id = $('#formaPagamentoId').val();
    const tipo = $('#tipo').val().trim();
    const status = $('#status').val();

    if (!tipo) {
        Swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Por favor, preencha o tipo de pagamento.'
        });
        return;
    }

    $.ajax({
        url: `/api/formas-pagamento/${id}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({ 
            tipo: tipo,
            status: status 
        }),
        success: function() {
            $('#formaPagamentoModal').modal('hide');
            $('#tabelaFormasPagamento').DataTable().ajax.reload();
            Swal.fire({
                icon: 'success',
                title: 'Sucesso',
                text: 'Forma de pagamento atualizada com sucesso!'
            });
        },
        error: function(xhr) {
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: xhr.responseText || 'Erro ao atualizar forma de pagamento.'
            });
        }
    });
}

function desativarFormaPagamento(id) {
    Swal.fire({
        title: 'Confirmar desativação',
        text: "Deseja realmente desativar esta forma de pagamento?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, desativar!',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `/api/formas-pagamento/${id}`,
                type: 'DELETE',
                success: function() {
                    $('#tabelaFormasPagamento').DataTable().ajax.reload();
                    Swal.fire(
                        'Desativada!',
                        'A forma de pagamento foi desativada com sucesso.',
                        'success'
                    );
                },
                error: function() {
                    Swal.fire(
                        'Erro!',
                        'Erro ao desativar forma de pagamento.',
                        'error'
                    );
                }
            });
        }
    });
}

function filtrarFormasPagamento() {
    const tipo = $('#filtroTipo').val();
    const status = $('#filtroStatus').val();

    let url = '/api/formas-pagamento/filtro?';
    if (tipo) url += `tipo=${encodeURIComponent(tipo)}&`;
    if (status) url += `status=${encodeURIComponent(status)}&`;

    $('#tabelaFormasPagamento').DataTable().ajax.url(url).load();
}

function limparFiltros() {
    $('#filtroForm')[0].reset();
    $('#tabelaFormasPagamento').DataTable().ajax.url('/api/formas-pagamento').load();
}

function logoff() {
    window.location.href = "/logout";
}

function editarUsuario() {
    window.location.href = "/editar-usuario";
} 