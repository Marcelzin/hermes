function deletarTarefa(id) {
    Swal.fire({
        title: 'Tem certeza?',
        text: "Você não poderá reverter esta ação!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, deletar!',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `/api/todos/${id}`,
                type: 'DELETE',
                success: function () {
                    Swal.fire(
                        'Deletado!',
                        'A tarefa foi deletada com sucesso.',
                        'success'
                    ).then(() => {
                        window.location.reload();
                    });
                },
                error: function () {
                    Swal.fire(
                        'Erro!',
                        'Não foi possível deletar a tarefa.',
                        'error'
                    );
                }
            });
        }
    });
}

function editarTarefa(id) {
    // Primeiro, buscar os dados da tarefa
    $.ajax({
        url: `/api/todos/${id}`,
        type: 'GET',
        success: function (tarefa) {
            // Preenche os campos da modal
            $('#titulo').val(tarefa.titulo);
            $('#descricao').val(tarefa.descricao);
            $('#status').val(tarefa.concluido ? '1' : '0');

            // Exibe a modal
            const modal = new bootstrap.Modal(document.getElementById('editarTarefaModal'));
            modal.show();

            // Handler para o botão de salvar
            $('#salvarEdicao').off('click').on('click', function () {
                const dadosAtualizados = {
                    titulo: $('#titulo').val(),
                    descricao: $('#descricao').val(),
                    concluido: parseInt($('#status').val())
                };

                $.ajax({
                    url: `/api/todos/${id}`,
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(dadosAtualizados),
                    success: function () {
                        modal.hide();
                        // Mostra mensagem de sucesso
                        Swal.fire({
                            icon: 'success',
                            title: 'Atualizado!',
                            text: 'A tarefa foi atualizada com sucesso.'
                        }).then(() => {
                            window.location.reload();
                        });
                    },
                    error: function () {
                        Swal.fire({
                            icon: 'error',
                            title: 'Erro!',
                            text: 'Não foi possível atualizar a tarefa.'
                        });
                    }
                });
            });
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Erro!',
                text: 'Não foi possível carregar os dados da tarefa.'
            });
        }
    });
}

function novaTarefa() {
    // Limpar o formulário
    $('#editarTarefaForm')[0].reset();
    
    // Abrir o modal
    $('#editarTarefaModalLabel').text('Nova Tarefa');
    $('#editarTarefaModal').modal('show');
    
    // Modificar o comportamento do botão salvar para criar uma nova tarefa
    $('#salvarEdicao').off('click').on('click', function() {
        const novaTarefa = {
            titulo: $('#titulo').val(),
            descricao: $('#descricao').val(),
            concluido: parseInt($('#status').val())
        };

        $.ajax({
            url: '/api/todos',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(novaTarefa),
            success: function() {
                $('#editarTarefaModal').modal('hide');
                Swal.fire({
                    icon: 'success',
                    title: 'Sucesso!',
                    text: 'Tarefa criada com sucesso.'
                }).then(() => {
                    window.location.reload();
                });
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Erro!',
                    text: 'Não foi possível criar a tarefa.'
                });
            }
        });
    });
}

$(document).ready(function() {
    $('#tabelaTarefas').DataTable({
        language: {
            url: 'https://cdn.datatables.net/plug-ins/1.13.7/i18n/pt-BR.json'
        },
        responsive: true,
        pageLength: 10,
        order: [[0, 'desc']]
    });
});