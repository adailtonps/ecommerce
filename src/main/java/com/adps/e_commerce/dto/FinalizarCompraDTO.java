package com.adps.e_commerce.dto;

import com.adps.e_commerce.domain.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinalizarCompraDTO {
    private String mensagem;
    private Pedido pedido;
}
