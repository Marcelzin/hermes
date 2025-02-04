package com.tcc.pdv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    @OneToMany(mappedBy = "comercio")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "comercio")
    private List<Produto> produtos;

    @OneToMany(mappedBy = "comercio")
    private List<Pedido> pedidos;

    // Getters and Setters
}