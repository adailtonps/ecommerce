package com.adps.e_commerce.dto;

import com.adps.e_commerce.enums.StatusUsuario;
import com.adps.e_commerce.enums.UsuarioRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCadastroDTO {
    private String idUsuario;
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private StatusUsuario statusUsuario;
    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private UsuarioRole usuarioRole;
}
