package com.tcc.pdv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @Column(name = "valor_fabrica", columnDefinition = "DECIMAL(10, 2) DEFAULT '0.00'")
    @Digits(integer = 8, fraction = 2)
    private double valorFabrica;

    @Column(name = "valor_venda", columnDefinition = "DECIMAL(10, 2) DEFAULT '0.00'")
    @Digits(integer = 8, fraction = 2)
    private double valorVenda;

    @Column(name = "barra")
    private long barra;

    private String descricao;

    private String imagem;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    private String status;

    @Column(name = "quantidade")
    private Integer quantidade = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorFabrica() {
        return valorFabrica;
    }

    public void setValorFabrica(double valorFabrica) {
        this.valorFabrica = valorFabrica;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getBarra() {
        return barra;
    }

    public void setBarra(long barra) {
        this.barra = barra;
    }

    public Integer getQuantidade() {
        return quantidade != null ? quantidade : 0;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}