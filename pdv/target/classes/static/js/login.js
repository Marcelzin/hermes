function login() {
    $.ajax({
        url: "/api/usuarios/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            email: $("#email").val().trim(),
            senha: $("#senha").val().trim(),
        }),
        success: function (response) {
            Swal.fire({
                icon: "success",
                title: "Sucesso",
                text: response.message,
            }).then(function() {
                window.location.href = "/home";
            });
        },
        error: function (xhr) {
            let errorMessage = "Ocorreu um erro ao realizar o login.";
            if (xhr.responseJSON && xhr.responseJSON.error) {
                errorMessage = xhr.responseJSON.error;
            }
            Swal.fire({
                icon: "error",
                title: "Erro",
                text: errorMessage,
            });
        }
    });
}