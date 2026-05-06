package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    @Override
    Optional<Categoria> findById(Integer idCategoria);
    Categoria findByNomeCategoria(String nomeCategoria);
    boolean existsByNomeCategoriaIgnoreCase(String nomeCategoria);
}
