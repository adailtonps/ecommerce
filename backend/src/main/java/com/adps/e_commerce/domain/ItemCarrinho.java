package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ItemCarrinho {
    @Id
    private String idItem;

    @PrePersist
    public void gerarId(){
        if(this.idItem == null){
            this.idItem = UUID.randomUUID().toString()
                    .replace("-","")
                    .toUpperCase()
                    .substring(0, 8);

        }
    }

    @ManyToOne
    @JoinColumn(name="id_carrinho_do_user")
    private Carrinho carrinho;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    private String idUsuario;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal precoTotal;

    private StatusItem statusItem;
}
