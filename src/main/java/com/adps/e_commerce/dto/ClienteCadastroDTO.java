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
    private String idCliente;
    private String nomeCliente;
    private String telefone;
    private String endereco;
    private String email;
    private String senha;
    private StatusUsuario statusUsuario;
    private UsuarioRole usuarioRole;
}
