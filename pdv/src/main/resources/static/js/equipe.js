function logoff() {
    window.location.href = "/logout";
}

function editarUsuario() {
    window.location.href = "/editar-usuario";
}

$(document).ready(function () {
    var table = $('#tabelaUsuarios').DataTable();

    $(document).ready(function () {
        $.ajax({
            url: '/api/usuarios',
            type: 'GET',
            success: function (data) {
                var table = $('#tabelaUsuarios').DataTable();
                table.clear().rows.add(data.map(usuario => [
                    usuario.nome,
                    usuario.email,
                    usuario.nivelAcesso,
                    usuario.status,
                    `<div class="d-flex gap-2 justify-content-center">
                        <button class="btn btn-warning btn-sm" onclick="editarUsuario(${usuario.id})">
                            <i class="fas fa-edit"></i> Editar
                        </button>
                        <button class="btn btn-danger btn-sm" onclick="desativarUsuario(${usuario.id})">
                            <i class="fas fa-ban"></i> Desabilitar
                        </button>
                    </div>`
                ])).draw();
            },
            error: function (xhr) {
                alert('Erro ao carregar usuários: ' + xhr.responseText);
            }
        });
    });

    $('#filtroForm input, #filtroForm select').on('keyup change', function () {
        table.draw();
    });
});

function novoUsuario() {
    document.getElementById('usuarioForm').reset();
    $('#usuarioModalLabel').text('Novo Usuário');
    $('#salvarUsuarioBtn').show();
    $('#atualizarUsuarioBtn').hide();
    $('#usuarioModal').modal('show');
}

function salvarUsuario() {
    const usuario = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        nivelAcesso: document.getElementById('nivelAcesso').value,
        status: "ATIVO"
    };

    $.ajax({
        url: '/api/usuarios',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(usuario),
        success: function (response) {
            $('#usuarioModal').modal('hide');
            location.reload();
        },
        error: function (xhr) {
            alert('Erro ao salvar usuário: ' + xhr.responseText);
        }
    });
}

function editarUsuario(id) {
    $.ajax({
        url: '/api/usuarios/' + id,
        type: 'GET',
        success: function (usuario) {
            $('#usuarioId').val(usuario.id);
            $('#nome').val(usuario.nome);
            $('#email').val(usuario.email);
            $('#senha').val(''); // Não preencher a senha
            $('#nivelAcesso').val(usuario.nivelAcesso);
            $('#status').val(usuario.status);
            $('#usuarioModalLabel').text('Editar Usuário');
            $('#salvarUsuarioBtn').hide();
            $('#atualizarUsuarioBtn').show();
            $('#usuarioModal').modal('show');
        },
        error: function (xhr) {
            alert('Erro ao carregar usuário: ' + xhr.responseText);
        }
    });
}

function atualizarUsuario() {
    const id = $('#usuarioId').val();
    const usuario = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        nivelAcesso: document.getElementById('nivelAcesso').value,
        status: document.getElementById('status').value,
    };

    $.ajax({
        url: '/api/usuarios/' + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(usuario),
        success: function (response) {
            $('#usuarioModal').modal('hide');
            location.reload();
        },
        error: function (xhr) {
            alert('Erro ao atualizar usuário: ' + xhr.responseText);
        }
    });
}

function desativarUsuario(id) {
    if (confirm('Tem certeza que deseja desativar este usuário?')) {
        $.ajax({
            url: '/api/usuarios/' + id,
            type: 'DELETE',
            success: function () {
                location.reload();
            },
            error: function (xhr) {
                alert('Erro ao desativar usuário: ' + xhr.responseText);
            }
        });
    }
}

function filtrarUsuarios() {
    const nome = $('#filtroNome').val();
    const email = $('#filtroEmail').val();
    const status = $('#filtroStatus').val();

    $.ajax({
        url: '/api/usuarios/filtro',
        type: 'GET',
        data: {
            nome: nome,
            email: email,
            status: status
        },
        success: function (data) {
            var table = $('#tabelaUsuarios').DataTable();
            table.clear().rows.add(data.map(usuario => [
                usuario.nome,
                usuario.email,
                usuario.nivelAcesso,
                usuario.status,
                `<div class="d-flex gap-2 justify-content-center">
                    <button class="btn btn-warning btn-sm" onclick="editarUsuario(${usuario.id})">
                        <i class="fas fa-edit"></i> Editar
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="desativarUsuario(${usuario.id})">
                        <i class="fas fa-ban"></i> Desabilitar
                    </button>
                </div>`
            ])).draw();
        },
        error: function (xhr) {
            alert('Erro ao filtrar usuários: ' + xhr.responseText);
        }
    });
}

function limparFiltros() {
    $('#filtroForm')[0].reset();
    var table = $('#tabelaUsuarios').DataTable();
    table.search('').columns().search('').draw();
}