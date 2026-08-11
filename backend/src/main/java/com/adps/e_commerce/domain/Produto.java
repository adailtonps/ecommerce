package com.adps.e_commerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@EqualsAndHashCode(of="idProduto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "produto")
public class Produto {
    @Id
    @Column(name = "id_produto")
    private String idProduto;

    @PrePersist
    public void gerarId(){
        if(this.idProduto == null){
        this.idProduto = UUID.randomUUID().toString()
                .replace("-","")
                .toUpperCase()
                .substring(0, 10);
    }}

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private Integer qntEstoque;

    @ManyToOne
    @JoinColumn(name = "idCategoria", nullable = false)
    private Categoria categoria;

    @OneToMany
    private List<ItemCarrinho> itemCarrinho;
}
