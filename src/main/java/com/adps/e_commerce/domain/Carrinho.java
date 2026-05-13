package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusCarrinho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrinho;


    private BigDecimal valorTotal;

    private StatusCarrinho statusCarrinho;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    private LocalDateTime dataCriacao;

    @OneToMany
    private ItemCarrinho itemCarrinho;
}
