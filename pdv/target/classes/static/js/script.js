$(document).ready(function () {
    const $step1 = $("#step1");
    const $step2 = $("#step2");
    const $nextBtn = $("#nextBtn");
    const $backBtn = $("#backBtn");
    const $doneBtn = $("#doneBtn");
    const $progressBar = $("#progressBar");
    const $progressBarStep2 = $("#progressBarStep2");

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
        return true;
    }

    $nextBtn.on("click", function () {
        if (!validateStep1()) return;

        const nomeComercio = $("#nomeComercio").val().trim();
        const cpfCnpj = $("#cpfCnpj").val().trim();

        $.ajax({
            url: "/api/comercios",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                nome: nomeComercio,
                cpfCnpj: cpfCnpj,
            }),
            success: function (response) {
                Swal.fire({
                    icon: "success",
                    title: "Sucesso",
                    text: "Comércio cadastrado com sucesso!",
                }).then(() => {
                    $step1.hide();
                    $step2.show();
                    $progressBar.removeClass("bg-success").addClass("bg-secondary");
                    $progressBarStep2.show();
                });
            },
            error: function () {
                Swal.fire({
                    icon: "error",
                    title: "Erro",
                    text: "Ocorreu um erro ao cadastrar o comércio.",
                });
            },
        });
    });

    $backBtn.on("click", function () {
        $step2.hide();
        $step1.show();
        $progressBar.removeClass("bg-secondary").addClass("bg-success");
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
                nivelAcesso: "prop",
                status: "ATIVO",
            }),
            success: function () {
                Swal.fire({
                    icon: "success",
                    title: "Sucesso",
                    text: "Usuário cadastrado com sucesso!",
                });
            },
            error: function () {
                Swal.fire({
                    icon: "error",
                    title: "Erro",
                    text: "Ocorreu um erro ao cadastrar o usuário.",
                });
            },
        });
    });

    $("#cpfCnpj").inputmask({
        mask: ["999.999.999-99", "99.999.999/9999-99"],
        keepStatic: true,
        clearIncomplete: true,
    });
});