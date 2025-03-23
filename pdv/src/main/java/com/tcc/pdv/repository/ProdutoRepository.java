package com.tcc.pdv.repository;

import com.tcc.pdv.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>, JpaSpecificationExecutor<Produto> {
    List<Produto> findByComercioId(Integer comercioId);
    
    // Método para buscar por código de barras e comércio
    Optional<Produto> findByBarraAndComercioIdAndStatus(Long barra, Integer comercioId, String status);
}