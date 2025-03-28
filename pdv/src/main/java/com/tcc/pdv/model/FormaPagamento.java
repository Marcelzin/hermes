package com.tcc.pdv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FormaPagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "tipo", nullable = false)
	private String tipo;

	@Column(name = "status", nullable = false)
	private String status = "ATIVO";

	@ManyToOne
	@JoinColumn(name = "comercio_id", nullable = false)
	private Comercio comercio;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Comercio getComercio() {
		return comercio;
	}

	public void setComercio(Comercio comercio) {
		this.comercio = comercio;
	}

}