package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.enums.StatusCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho,Integer> {
    Optional<Carrinho> findByUsuarioAndStatusCarrinho(Usuario usuario, StatusCarrinho status);
    Carrinho findByUsuario(Usuario usuario);
}
