package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.ItemCarrinho;
import com.adps.e_commerce.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho,String> {
    ItemCarrinho findByCarrinhoAndProduto (Carrinho carrinho, Produto produto);
    List<ItemCarrinho> findByCarrinho (Carrinho carrinho);
    Optional<ItemCarrinho> findByIdItemAndCarrinho(String idItemCarrinho, Carrinho carrinho);

}
