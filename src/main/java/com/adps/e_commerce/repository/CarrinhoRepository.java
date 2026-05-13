package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Cliente;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.enums.StatusUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho,Integer> {
    Optional<Carrinho> findByClienteAndStatusCarrinho(Cliente cliente, StatusCarrinho status);
}
