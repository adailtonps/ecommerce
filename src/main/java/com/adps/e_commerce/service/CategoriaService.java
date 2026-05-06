package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Categoria;
import com.adps.e_commerce.dto.*;
import com.adps.e_commerce.exception.CategoriaNaoEncontradaException;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.CategoriaRepository;
import com.adps.e_commerce.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public CategoriaResponseDTO criarCategoria(CadastroCategoriaDTO categoria){
        Categoria novaCategoria = new Categoria();

        Categoria jaExisteCategoria = categoriaRepository.findByNomeCategoria(categoria.getNomeCategoria());

        if(jaExisteCategoria != null){
            throw new CategoriaNaoEncontradaException("Categoria já existente!");
        }

        String categoriaFormatada = categoria.getNomeCategoria().trim().toLowerCase(Locale.ROOT);

        if(categoriaRepository.existsByNomeCategoriaIgnoreCase(categoriaFormatada)){
            throw new RegradeNegocioException("Essa categoria já existe!");
        }

        novaCategoria.setNomeCategoria(categoriaFormatada);

        categoriaRepository.save(novaCategoria);

        return new CategoriaResponseDTO(
                novaCategoria.getIdCategoria(),
                novaCategoria.getNomeCategoria()
        );
    }

    public List<CategoriaResponseDTO> listarCategorias(){
        List<Categoria> categorias = categoriaRepository.findAll();

        if(categorias.isEmpty()){
            throw new CategoriaNaoEncontradaException("Nenhuma categoria encontrada!");
        }

        return categorias.stream()
                .map(categoriasCadastradas -> new CategoriaResponseDTO(
                        categoriasCadastradas.getIdCategoria(),
                        categoriasCadastradas.getNomeCategoria()
                )).toList();
    }

    @Transactional
    public CategoriaResponseDTO atualizarCategoria(AtualizaeCateogriaDTO atualizaeCateogriaDTO) {
        Categoria categoria = categoriaRepository.findById(atualizaeCateogriaDTO.getIdCategoria())
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não encontrada!"));

        categoria.setNomeCategoria(atualizaeCateogriaDTO.getNomeCategoria());
        Categoria CatAtualizada = categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(
                CatAtualizada.getIdCategoria(),
                CatAtualizada.getNomeCategoria()
        );
    }

    @Transactional
    public ResponseEntity<String> deletarCategoria(DeletarCategoriaDTO deletarCategoriaDTO){
        Categoria categoria = categoriaRepository.findById(deletarCategoriaDTO.getIdCategoria())
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não encontrada!"));

        produtoRepository.removeCategoriadosProdutos(categoria.getIdCategoria());
        categoriaRepository.deleteById(categoria.getIdCategoria());
        return ResponseEntity.ok().body("Categoria deletada!");
    }
}
