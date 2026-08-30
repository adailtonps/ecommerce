package com.adps.e_commerce.dto;

import com.adps.e_commerce.enums.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDTO {
    private String idUsuario;
    private String nome;
    private String email;
    private String telefone;
    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private StatusUsuario userStatus;
}
