// Frontend 

$(document).ready(function() {
    const $form1 = $(".form_1");
    const $form2 = $(".form_2");

    const $form1Btns = $(".form_1_btns");
    const $form2Btns = $(".form_2_btns");

    const $form1NextBtn = $(".form_1_btns .btn_next");
    const $form2BackBtn = $(".form_2_btns .btn_back");

    const $form2ProgressBar = $(".form_2_progessbar");

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

});

// Validações

// Mascara CPF/CNPJ
document.addEventListener("DOMContentLoaded", function () {
    // Seletor para o campo de CPF/CNPJ
    const cpfCnpj2 = document.getElementById("cpfCnpj");

    // Aplicar a máscara usando o Inputmask
    $(cpfCnpj2).inputmask({
        mask: ['999.999.999-99', '99.999.999/9999-99'],
        keepStatic: true,
        clearIncomplete: true,
    });
});


const next_btn = document.getElementsByClassName("btn_next");
const nomeComercio = document.getElementById("nomeComercio");
const cpfCnpj = document.getElementById("cpfCnpj");



/* btn_next
nomeComercio não pode ser vazio, deve ter no máximo 120 caracteres
cpfCnpj - não pode ser vazio
nomeCompleto - não pode ser vazio, no máximo 120 caracteres
email - não pode ser vazio, deve ser padrão email
senha - não pode ser vazio, deve ter no minimo 8 caracteres
repetirSenha - deve ser igual a senha anterior */

// Requisições