package com.tcc.pdv.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    private String email;

    private String senha;

    @Column(name = "nivel_acesso")
    private String nivelAcesso;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    private String status;

    // Getters and Setters
}