package com.adps.e_commerce.dto;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Produto;
import com.adps.e_commerce.enums.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinhoDTO {
    private String idItem;

    private Carrinho carrinho;

    private Produto produto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal precoTotal;

    private StatusItem statusItem;
}
