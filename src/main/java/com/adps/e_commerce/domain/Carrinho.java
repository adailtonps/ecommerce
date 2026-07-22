package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusCarrinho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrinho;


    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusCarrinho statusCarrinho;

    @OneToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "carrinho")
    private List<ItemCarrinho> itemCarrinho;
}
