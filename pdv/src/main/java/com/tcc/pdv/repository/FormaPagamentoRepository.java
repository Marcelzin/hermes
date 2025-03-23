package com.tcc.pdv.repository;

import com.tcc.pdv.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Integer> {
    List<FormaPagamento> findByComercioIdAndStatus(Integer comercioId, String status);
}