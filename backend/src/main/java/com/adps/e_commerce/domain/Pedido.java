package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    private String idPedido;

    @PrePersist
    public void gerarDados() {
        if (this.idPedido == null) {
            this.idPedido = UUID.randomUUID().toString()
                    .replace("-", "")
                    .toUpperCase()
                    .substring(0, 8);

        }
    }

    @ManyToOne
    private Usuario usuario;

    private BigDecimal valorTotal;

    private String codigoPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    private LocalDateTime dataPedido;

    private LocalDateTime dataPagamento;

    @OneToMany(mappedBy = "pedido")
    private List<itemPedido> itemPedido;

    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
}
