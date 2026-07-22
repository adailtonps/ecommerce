package com.adps.e_commerce.dto;

import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.domain.itemPedido;
import com.adps.e_commerce.enums.PedidoStatus;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PedidoResponseDTO {
    private String idPedido;
    private BigDecimal valorTotal;
    private PedidoStatus pedidoStatus;
    private LocalDate dataPagamento;
    private LocalDate dataPedido;

    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private List<ItemPedidoResponseDTO> itens;
}
