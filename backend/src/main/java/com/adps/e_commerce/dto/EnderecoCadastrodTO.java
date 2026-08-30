package com.adps.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoCadastrodTO {
    @NotBlank
    private String rua;

    @NotNull
    private Integer numero;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    @NotNull
    @Pattern(regexp = "\\d{8}",message = "CEP deve conter 8 dígitos!")
    private String cep;

    private String complemento;
}
