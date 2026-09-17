package com.adps.e_commerce.dto;

import com.adps.e_commerce.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusPagamentoDTO {
    private String idPedido;
    private StatusPedido statusPagamento;
    private LocalDateTime dataPagamento;
    private Long id_do_pagador;
    private String nome_do_pagador;
}
