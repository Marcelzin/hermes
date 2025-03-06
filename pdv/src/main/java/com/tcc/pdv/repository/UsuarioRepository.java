package com.tcc.pdv.repository;

import com.tcc.pdv.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario>  {
    List<Usuario> findAll();
    Usuario findByEmail(String email);
    List<Usuario> findByComercio_idAndIdNot(Integer comercio_id, Integer userId);
}