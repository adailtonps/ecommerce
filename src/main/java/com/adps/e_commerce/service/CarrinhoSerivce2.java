package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.domain.ItemCarrinho;
import com.adps.e_commerce.dto.ItemCarrinhoDTO;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.CarrinhoRepository;
import com.adps.e_commerce.repository.UsuarioRepository;
import com.adps.e_commerce.repository.ItemCarrinhoRepository;
import com.adps.e_commerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrinhoSerivce2 {
    @Autowired
    private ItemCarrinhoRepository itemCarrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ItemCarrinhoDTO> mostrarItensCarrinho(Usuario usuarioLogado) {
        Carrinho carrinho = carrinhoRepository.findByUsuario(usuarioLogado);

        if (carrinho == null) {
            throw new RegradeNegocioException("Nenhum carrinho encontrado!");
        }
        List<ItemCarrinho> itensNoCarrinho = itemCarrinhoRepository.findByCarrinho(carrinho);

        if (itensNoCarrinho.isEmpty()) {
            throw new RegradeNegocioException("Nenhum item encontrado no carrinho!");
        }
        return itensNoCarrinho.stream()
                .map(produtosNoCarrinho -> new ItemCarrinhoDTO(
                        produtosNoCarrinho.getIdItem(),
                        produtosNoCarrinho.getCarrinho(),
                        produtosNoCarrinho.getProduto(),
                        produtosNoCarrinho.getQuantidade(),
                        produtosNoCarrinho.getPrecoUnitario(),
                        produtosNoCarrinho.getPrecoTotal(),
                        produtosNoCarrinho.getStatusItem()
                )).toList();
    }
}
