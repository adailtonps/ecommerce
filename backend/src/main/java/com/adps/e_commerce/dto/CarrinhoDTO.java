package com.adps.e_commerce.dto;

import com.adps.e_commerce.enums.StatusCarrinho;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CarrinhoDTO {
    private StatusCarrinho statusCarrinho;
    private Integer idCliente;
    private LocalDateTime dataCriacao;
    private BigDecimal valorTotal;



}
