package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Categoria;
import com.adps.e_commerce.domain.Produto;
import com.adps.e_commerce.dto.ApagarProdutoDTO;
import com.adps.e_commerce.dto.AtualizarProdutoDTO;
import com.adps.e_commerce.dto.CadastroProdutoDTO;
import com.adps.e_commerce.dto.ProdutoResponseDTO;
import com.adps.e_commerce.exception.CategoriaNaoEncontradaException;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.exception.ProdutoNaoEncontradoException;
import com.adps.e_commerce.repository.CategoriaRepository;
import com.adps.e_commerce.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,  CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProdutoResponseDTO cadastrarProduto(CadastroProdutoDTO produto){
        Produto novoProduto = new Produto();

        Categoria categoria = categoriaRepository.findById(produto.getIdCategoria())
                .orElseThrow(() -> new RegradeNegocioException("Categoria não encontrada!"));

        if(produto.getNome() == null || produto.getNome().isBlank()){
            throw new RegradeNegocioException("Nome obrigatório!");
        }
        if(produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0){
            throw new RegradeNegocioException("Preço obrigatório!");
        }
        if(produto.getDescricao() == null || produto.getDescricao().isBlank()){
            throw new RegradeNegocioException("Descrição obrigatória!");
        }
        if (produto.getQntEstoque() == null || produto.getQntEstoque() <= 0){
            throw new RegradeNegocioException("Produto sem estoque definido!");
        }

        novoProduto.setCategoria(categoria);
        novoProduto.setNome(produto.getNome());
        novoProduto.setPreco(produto.getPreco());
        novoProduto.setDescricao(produto.getDescricao());
        novoProduto.setQntEstoque(produto.getQntEstoque());

        Produto produtoSalvo = produtoRepository.save(novoProduto);
        return new ProdutoResponseDTO(
                produtoSalvo.getIdProduto(),
                produtoSalvo.getNome(),
                produtoSalvo.getPreco(),
                produtoSalvo.getDescricao(),
                categoria.getIdCategoria(),
                categoria.getNomeCategoria(),
                produtoSalvo.getQntEstoque()
        );
    }

    public List<ProdutoResponseDTO> listarProdutosCadastrados(String nome, String idProduto, Integer idCategoria, BigDecimal preco){
        List<Produto> produtosCadastrados;
        if(idProduto != null && !idProduto.isBlank()){
            produtosCadastrados = produtoRepository.findById(idProduto)
                    .map(List::of)
                    .orElse(List.of());

        } else if(nome != null && !nome.isBlank() && idCategoria != null){
            produtosCadastrados = produtoRepository.findByNomeContainingIgnoreCaseAndCategoriaIdCategoria(nome, idCategoria);
        } else if(nome != null && !nome.isBlank()){
            produtosCadastrados = produtoRepository.findByNomeContainingIgnoreCase(nome);
        } else if(idCategoria != null){
            produtosCadastrados = produtoRepository.findByCategoriaIdCategoria(idCategoria);
        } else if (preco != null){
            produtosCadastrados = produtoRepository.findByPrecoLessThanEqual(preco);
        } else {
            throw new RegradeNegocioException("Parâmetro inválido!");
        }
        if(produtosCadastrados.isEmpty()){
            throw new RegradeNegocioException("Nenhum produto cadastrado!");
        }
        return produtosCadastrados.stream()
                .map(itemCadastrado -> new ProdutoResponseDTO(
                        itemCadastrado.getIdProduto(),
                        itemCadastrado.getNome(),
                        itemCadastrado.getPreco(),
                        itemCadastrado.getDescricao(),
                        itemCadastrado.getCategoria() != null
                            ? itemCadastrado.getCategoria().getIdCategoria()
                            : null,
                        itemCadastrado.getCategoria() != null
                            ? itemCadastrado.getCategoria().getNomeCategoria()
                            : "Sem categoria!",
                        itemCadastrado.getQntEstoque()
                )
                ).toList();
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(AtualizarProdutoDTO produto){
        Produto atualizarProduto = produtoRepository.findById(produto.getIdProduto())
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado!"));

        Categoria categoria = categoriaRepository.findById(produto.getIdCategoria())
                .orElseThrow(()-> new CategoriaNaoEncontradaException("Categoria não encontrada!"));

        if(produto.getPreco() != null && produto.getPreco().compareTo(BigDecimal.ZERO) <= 0){
            throw new RegradeNegocioException("Preencha o preço corretamente!");
        }
        if(produto.getNome() != null && produto.getNome().isBlank()){
            throw new RegradeNegocioException("Preencha o nome corretamente!");
        }
        if(produto.getDescricao() != null && produto.getDescricao().isBlank()){
            throw new RegradeNegocioException("Preencha a descrição corretamente!");
        }
        if(produto.getQtnEstoque() != null && produto.getQtnEstoque().intValue() < 0){
            throw new RegradeNegocioException("Estoque não pode ser menor que 0!");
        }

        if(produto.getNome() != null){
            atualizarProduto.setNome(produto.getNome());
        }
        if(produto.getPreco() != null){
            atualizarProduto.setPreco(produto.getPreco());
        }
        if(produto.getDescricao() != null){
            atualizarProduto.setDescricao(produto.getDescricao());
        }
        if(produto.getIdCategoria() != null){
            Categoria categorias = categoriaRepository.findById(produto.getIdCategoria())
                    .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não encontrada!"));
            atualizarProduto.setCategoria(categorias);
        }
        if(produto.getQtnEstoque() != null){
            atualizarProduto.setQntEstoque(produto.getQtnEstoque());
        }

        Produto salvarProdutoAtualizado = produtoRepository.save(atualizarProduto);

        return new ProdutoResponseDTO(
                salvarProdutoAtualizado.getIdProduto(),
                salvarProdutoAtualizado.getNome(),
                salvarProdutoAtualizado.getPreco(),
                salvarProdutoAtualizado.getDescricao(),
                salvarProdutoAtualizado.getCategoria().getIdCategoria(),
                salvarProdutoAtualizado.getCategoria().getNomeCategoria(),
                salvarProdutoAtualizado.getQntEstoque()
        );
    }

    @Transactional
    public ResponseEntity<String> deletarProduto(ApagarProdutoDTO apagarProdutoDTO){
        Produto apagarProduto = produtoRepository.findById(apagarProdutoDTO.getIdProduto())
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado!"));

        produtoRepository.delete(apagarProduto);

        return ResponseEntity.ok("Produto deletado com sucesso!");
    }
}
