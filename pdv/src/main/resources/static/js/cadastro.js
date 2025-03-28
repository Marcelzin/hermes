$(document).ready(function () {
    const $step1 = $("#step1");
    const $step2 = $("#step2");
    const $nextBtn = $("#nextBtn");
    const $backBtn = $("#backBtn");
    const $doneBtn = $("#doneBtn");
    const $progressBar = $("#progressBar");
    const $progressBarStep2 = $("#progressBarStep2");

    let comercioId = null;

    function validateStep1() {
        const nomeComercio = $("#nomeComercio").val().trim();
        const cpfCnpj = $("#cpfCnpj").val().trim();

        if (!nomeComercio || !cpfCnpj) {
            Swal.fire({
                icon: "error",
                title: "Erro",
                text: "Por favor, preencha todos os campos obrigatórios.",
            });
            return false;
        }
        return true;
    }

    function validateStep2() {
        const nomeCompleto = $("#nomeCompleto").val().trim();
        const email = $("#email").val().trim();
        const senha = $("#senha").val().trim();
        const repetirSenha = $("#repetirSenha").val().trim();

        if (!nomeCompleto || !email || !senha || !repetirSenha) {
            Swal.fire({
                icon: "error",
                title: "Erro",
                text: "Por favor, preencha todos os campos obrigatórios.",
            });
            return false;
        }

        if (senha !== repetirSenha) {
            Swal.fire({
                icon: "error",
                title: "Erro",
                text: "As senhas não coincidem.",
            });
            return false;
        }

        if (senha.length < 8) {
            Swal.fire({
                icon: "error",
                title: "Erro",
                text: "A senha deve ter pelo menos 8 caracteres.",
            });
            return false;
        }

        return true;
    }

    $nextBtn.on("click", function () {
        if (!validateStep1()) return;

        $.ajax({
            url: "/api/comercios",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                nome: $("#nomeComercio").val().trim(),
                cpfCnpj: $("#cpfCnpj").val().trim()
            }),
            success: function (response) {
                comercioId = response.id;
                $step1.hide();
                $step2.show();
                $progressBar.removeClass("bg-secondary").addClass("bg-success");
                $progressBarStep2.show();
            },
            error: function (xhr) {
                let errorMessage = "Ocorreu um erro ao cadastrar o comércio.";
                if (xhr.status === 400 && xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                Swal.fire({
                    icon: "error",
                    title: "Erro",
                    text: errorMessage,
                });
            }
        });
    });

    $backBtn.on("click", function () {
        $step2.hide();
        $step1.show();
        $progressBar.removeClass("bg-success").addClass("bg-secondary");
        $progressBarStep2.hide();
    });

    $doneBtn.on("click", function () {
        if (!validateStep2()) return;

        $.ajax({
            url: "/api/usuarios",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                nome: $("#nomeCompleto").val().trim(),
                email: $("#email").val().trim(),
                senha: $("#senha").val().trim(),
                comercioId: comercioId,
                nivelAcesso: "prop",
                status: "ATIVO"
            }),
            success: function () {
                Swal.fire({
                    icon: "success",
                    title: "Sucesso",
                    text: "Usuário cadastrado com sucesso!",
                }).then(function () {
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
                            }).then(function () {
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
                });
            },
            error: function (xhr) {
                let errorMessage = "Ocorreu um erro ao cadastrar o usuário.";
                if (xhr.status === 409) {
                    errorMessage = "E-mail já cadastrado.";
                }
                Swal.fire({
                    icon: "error",
                    title: "Erro",
                    text: errorMessage,
                });
            }
        });
    });

    $("#cpfCnpj").inputmask({
        mask: ["999.999.999-99", "99.999.999/9999-99"],
        keepStatic: true,
        clearIncomplete: true,
    });
});