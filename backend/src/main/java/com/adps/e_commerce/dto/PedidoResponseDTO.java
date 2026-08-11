package com.adps.e_commerce.dto;

import com.adps.e_commerce.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PedidoResponseDTO {
    private String idPedido;
    private BigDecimal valorTotal;
    private StatusPedido statusPedido;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataPedido;

    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private List<ItemPedidoResponseDTO> itens;
}
