package com.adps.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponseDTO {
    private String idProduto;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer idCategoria;
    private String nomeCategoria;
}
