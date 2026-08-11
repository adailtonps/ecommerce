package com.adps.e_commerce.domain;

import com.adps.e_commerce.enums.StatusUsuario;
import com.adps.e_commerce.enums.UsuarioRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    private String idUsuario;

    @PrePersist
    public void gerarId(){
        if(this.idUsuario == null){
            this.idUsuario = UUID.randomUUID().toString()
                    .replace("-","")
                    .toUpperCase()
                    .substring(0, 8);
    }}

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String telefone;


    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;

    @JsonIgnore
    private String senha;

    @Enumerated(EnumType.STRING)
    private UsuarioRole userRole;

    @Enumerated(EnumType.STRING)
    private StatusUsuario statusUser;

    @OneToOne(mappedBy = "usuario")
    private Carrinho carrinho;
}
