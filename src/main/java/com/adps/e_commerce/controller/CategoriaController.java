package com.adps.e_commerce.controller;

import com.adps.e_commerce.dto.AtualizaeCateogriaDTO;
import com.adps.e_commerce.dto.CadastroCategoriaDTO;
import com.adps.e_commerce.dto.CategoriaResponseDTO;
import com.adps.e_commerce.dto.DeletarCategoriaDTO;
import com.adps.e_commerce.repository.CategoriaRepository;
import com.adps.e_commerce.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody CadastroCategoriaDTO cadastroCategoriaDTO){
        CategoriaResponseDTO novaCategoria = categoriaService.criarCategoria(cadastroCategoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias(){
        List<CategoriaResponseDTO> categotiasCadastradas = categoriaService.listarCategorias();
        return ResponseEntity.ok(categotiasCadastradas);
    }

    @PutMapping
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(@RequestBody AtualizaeCateogriaDTO atualizaeCateogriaDTO){
        CategoriaResponseDTO categoriaAtualizada = categoriaService.atualizarCategoria(atualizaeCateogriaDTO);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping
    public ResponseEntity<String> apagarCategoria(@RequestBody DeletarCategoriaDTO deletarCategoriaDTO){
        return categoriaService.deletarCategoria(deletarCategoriaDTO);
    }
}
