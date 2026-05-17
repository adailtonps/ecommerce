package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusUsuario;
import com.adps.e_commerce.enums.UsuarioRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    private String idCliente;

    @PrePersist
    public void gerarId(){
        if(this.idCliente == null){
            this.idCliente = UUID.randomUUID().toString()
                    .replace("-","")
                    .toUpperCase()
                    .substring(0, 8);
    }}

    private String nomeCliente;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String telefone;

    private String endereco;

    @JsonIgnore
    private String senha;

    @Enumerated(EnumType.STRING)
    private UsuarioRole userRole;

    @Enumerated(EnumType.STRING)
    private StatusUsuario statusUser;

    @OneToMany(mappedBy = "cliente")
    private Carrinho carrinho;
}
