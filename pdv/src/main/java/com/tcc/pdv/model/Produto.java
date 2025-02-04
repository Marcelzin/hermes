package com.tcc.pdv.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @Column(name = "valor_fabrica")
    private double valorFabrica;

    @Column(name = "valor_venda")
    private double valorVenda;

    private String descricao;

    private String imagem;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    private String status;

    // Getters and Setters
}