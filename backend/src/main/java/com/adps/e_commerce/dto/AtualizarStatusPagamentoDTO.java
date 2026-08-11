package com.adps.e_commerce.dto;

import com.adps.e_commerce.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusPagamentoDTO {
    private String idPedido;
    private StatusPedido statusPedido;
}
