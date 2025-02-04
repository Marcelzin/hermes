$(document).ready(function() {
    const $form1 = $(".form_1");
    const $form2 = $(".form_2");

    const $form1Btns = $(".form_1_btns");
    const $form2Btns = $(".form_2_btns");

    const $form1NextBtn = $(".form_1_btns .btn_next");
    const $form2BackBtn = $(".form_2_btns .btn_back");

    const $form2ProgressBar = $(".form_2_progessbar");

    const $btnDone = $(".btn_done");
    const $modalWrapper = $(".modal_wrapper");
    const $shadow = $(".shadow");

    // Apply mask to CPF/CNPJ field
    $('#cpfCnpj').mask('000.000.000-00', {
        onKeyPress: function (cpfCnpj, e, field, options) {
            const masks = ['000.000.000-00', '00.000.000/0000-00'];
            const mask = (cpfCnpj.length > 14) ? masks[1] : masks[0];
            $('#cpfCnpj').mask(mask, options);
        }
    });

    $form1NextBtn.on("click", function() {
        $form1.hide();
        $form2.show();

        $form1Btns.hide();
        $form2Btns.css("display", "flex");

        $form2ProgressBar.addClass("active");
    });

    $form2BackBtn.on("click", function() {
        $form1.show();
        $form2.hide();

        $form1Btns.css("display", "flex");
        $form2Btns.hide();

        $form2ProgressBar.removeClass("active");
    });

    $btnDone.on("click", function() {
        if (validateForm()) {
            const formData = {
                nomeComercio: $('#nomeComercio').val(),
                cpfCnpj: $('#cpfCnpj').val(),
                nomeCompleto: $('#nomeCompleto').val(),
                email: $('#email').val(),
                senha: $('#senha').val()
            };

            $.ajax({
                url: '/your-endpoint-url',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) {
                    $modalWrapper.addClass("active");
                },
                error: function(error) {
                    alert('Erro ao enviar os dados. Tente novamente.');
                }
            });
        }
    });

    $shadow.on("click", function() {
        $modalWrapper.removeClass("active");
    });

    function validateForm() {
        const cpfCnpj = $('#cpfCnpj').val();
        const email = $('#email').val();
        const senha = $('#senha').val();
        const repetirSenha = $('#repetirSenha').val();

        const cpfCnpjPattern = /(^\d{3}\.\d{3}\.\d{3}-\d{2}$)|(^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$)/;
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!cpfCnpjPattern.test(cpfCnpj)) {
            alert('CPF ou CNPJ inválido.');
            return false;
        }

        if (!emailPattern.test(email)) {
            alert('Email inválido.');
            return false;
        }

        if (senha.length < 8) {
            alert('A senha deve ter pelo menos 8 caracteres.');
            return false;
        }

        if (senha !== repetirSenha) {
            alert('As senhas não coincidem.');
            return false;
        }

        return true;
    }
});