package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto,String> {
    Optional<Produto> findByIdProduto(String idProduto);
    @Modifying
    @Transactional
    @Query("""
            UPDATE Produto p
            SET p.categoria = null
            WHERE p.categoria.idCategoria = :idCategoria""")
    void removeCategoriadosProdutos(@Param("idCategoria") Integer idCategoria);
    List<Produto> findByCategoriaIdCategoria(Integer idCategoria);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByNomeContainingIgnoreCaseAndCategoriaIdCategoria(String nome, Integer idCategoria);
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

}
