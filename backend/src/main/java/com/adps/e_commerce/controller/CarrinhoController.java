package com.adps.e_commerce.controller;

import com.adps.e_commerce.domain.Produto;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.AdicionarProdutoDTO;
import com.adps.e_commerce.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {
    private final CarrinhoService carrinhoService;

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionarProdutoNoCarrinho(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestBody AdicionarProdutoDTO produtoDto){

        carrinhoService.adicionarProduto(usuarioLogado, produtoDto);
        return ResponseEntity.ok().body("Produto adicionado com sucesso!");
    }

}
