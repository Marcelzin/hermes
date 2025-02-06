package com.tcc.pdv.repository;

import com.tcc.pdv.model.Comercio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Integer> {
    List<Comercio> findByCpfCnpj(String cpfCnpj);
}