package com.adps.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoResponseDTO {
    private String mensagem;
    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private Integer cep;
    private String complemento;

}
