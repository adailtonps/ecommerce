package com.adps.e_commerce.controller;

import com.adps.e_commerce.dto.*;
import com.adps.e_commerce.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody CadastroProdutoDTO produto){
        ProdutoResponseDTO produtoCadastrado = produtoService.cadastrarProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProduto(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String idProduto,
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) BigDecimal preco
    ){
        List<ProdutoResponseDTO> produtosCadastrados = produtoService.listarProdutosCadastrados(nome, idProduto, idCategoria, preco);
        return ResponseEntity.ok(produtosCadastrados);
    }

    @PutMapping
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@RequestBody AtualizarProdutoDTO produto){
        ProdutoResponseDTO novoProduto = produtoService.atualizarProduto(produto);
        return ResponseEntity.ok(novoProduto);
    }

    @DeleteMapping
    public ResponseEntity<String>apagarProduto(@RequestBody ApagarProdutoDTO apagarProdutoDTO){
        return produtoService.deletarProduto(apagarProdutoDTO);
    }
}
