package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.ProdutoStatus;
import com.adps.e_commerce.enums.StatusCarrinho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    public void gerarId(){
        if(this.idPedido == null){
            this.idPedido = UUID.randomUUID().toString()
                    .replace("-","")
                    .toUpperCase()
                    .substring(0, 8);

        }
    }

    @ManyToOne
    private Usuario usuario;

    private Integer valorTotal;

    private ProdutoStatus produtoStatus;

    private String codigoPagamento;
    public void gerarCodigoPagamento(){
        if(this.codigoPagamento == null){
            this.codigoPagamento = "PAY-"+
                    UUID.randomUUID()
                            .toString()
                            .replace("-","")
                            .toUpperCase()
                            .substring(0, 14);

        }
    }

    private LocalDate dataPagamento;

    @OneToMany(mappedBy = "produto")
    private List<itemProduto> itemProduto;

    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
}
