package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class itemPedido {
    @Id
    private String idItemProduto;

    @PrePersist
    public void gerarId(){
        if(this.idItemProduto == null){
            this.idItemProduto = UUID.randomUUID().toString()
                    .replace("-","")
                    .toUpperCase()
                    .substring(0, 8);

        }
    }

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name="produto")
    private Produto produto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal precoTotal;

    private StatusItem statusItem;
}
