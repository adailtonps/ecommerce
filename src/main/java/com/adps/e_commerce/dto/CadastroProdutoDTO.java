package com.adps.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CadastroProdutoDTO {
    private String idProduto;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer idCategoria;
    private String nomeCategoria;
    private Integer qntEstoque;
}
