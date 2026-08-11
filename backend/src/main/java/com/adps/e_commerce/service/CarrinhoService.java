package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.*;
import com.adps.e_commerce.dto.AdicionarProdutoDTO;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.enums.StatusItem;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CarrinhoService {
    private final ItemCarrinhoRepository itemCarrinhoRepository;

    private final CarrinhoRepository carrinhoRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProdutoRepository produtoRepository;

    public Carrinho buscarCarrinho(Usuario usuario) {
        Optional<Carrinho> carrinhoExistente = carrinhoRepository.findByUsuarioAndStatusCarrinho(usuario, StatusCarrinho.ATIVADO);
        if(carrinhoExistente.isPresent()) {
            return carrinhoExistente.get();
        } else {
            throw new RegradeNegocioException("Conta desativada!");
        }
    }

    public void adicionarProduto(Usuario usuario, AdicionarProdutoDTO adicionarProdutoDTO) {
        Produto produto = produtoRepository.findByIdProduto(adicionarProdutoDTO.getIdProduto())
                .orElseThrow(() -> new RegradeNegocioException("Produto não encontrado!"));

        if(produto.getQntEstoque() <= 0){
            throw new RegradeNegocioException("Esse produto não está disponível!");
        }

        Carrinho carrinho = buscarCarrinho(usuario);

        ItemCarrinho itemExiste = itemCarrinhoRepository.findByCarrinhoAndProduto(carrinho, produto);

        if(itemExiste != null){
            throw new RegradeNegocioException("Produto já adicionado no carrinho!");
        }

        ItemCarrinho novoItem = new ItemCarrinho();

        novoItem.setProduto(produto);
        novoItem.setCarrinho(carrinho);
        novoItem.setQuantidade(1);
        novoItem.setPrecoUnitario(produto.getPreco());
        novoItem.setPrecoTotal(produto.getPreco());
        novoItem.setStatusItem(StatusItem.NO_CARRINHO);

        carrinho.setQnt_total_de_unidades(carrinho.getQnt_total_de_unidades()+1);
        novoItem.setIdUsuario(carrinho.getUsuario().getIdUsuario());

        BigDecimal totalAtual = carrinho.getValorTotal() == null
                ? BigDecimal.ZERO
                : carrinho.getValorTotal();

        carrinho.setValorTotal(totalAtual.add(produto.getPreco()));

        itemCarrinhoRepository.save(novoItem);
        carrinhoRepository.save(carrinho);
    }

    public ResponseEntity<String> aumentarQuantidade(ItemCarrinho itemCarrinho, Produto produto, Carrinho carrinho, Usuario usuario) {
        ItemCarrinho itemExiste = itemCarrinhoRepository.findById(itemCarrinho.getIdItem())
                .orElseThrow(() -> new RegradeNegocioException("Esse item não está no carrinho!"));

        if(itemExiste.getQuantidade() + 1 > produto.getQntEstoque()){
            throw new RegradeNegocioException("Quantidade indisponível!");
        }

        if(carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO){
            throw new RegradeNegocioException("Carrinho desativado!");
        }

        if(!itemExiste.getCarrinho().getUsuario().equals(usuario)){
            throw new RegradeNegocioException("Carrinho não pertence ao cliente!");
        }

        itemExiste.setQuantidade(itemExiste.getQuantidade() + 1);
        BigDecimal qntConvertida= (BigDecimal.valueOf(itemExiste.getQuantidade())) ;
        itemExiste.setPrecoTotal(produto.getPreco().multiply(qntConvertida));
        BigDecimal totalAtual = carrinho.getValorTotal() == null
                ? BigDecimal.ZERO
                : carrinho.getValorTotal();

        carrinho.setValorTotal(totalAtual.add(produto.getPreco()));

        itemCarrinhoRepository.save(itemExiste);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok().body("Quantidade atualizada com sucesso!");
    }

    public ResponseEntity<String> diminuirQuantidade(ItemCarrinho itemCarrinho, Produto produto, Usuario usuarioLogado, Carrinho carrinho){
        ItemCarrinho itemExiste = itemCarrinhoRepository.findById(itemCarrinho.getIdItem())
                .orElseThrow(() -> new RegradeNegocioException("Esse item não está no carrinho!"));

        if(carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO){
            throw new RegradeNegocioException("Carrinho desativado!");
        }

        if(!itemExiste.getCarrinho().getUsuario().equals(usuarioLogado)){
            throw new RegradeNegocioException("Carrinho não pertence ao cliente!");
        }

        if(itemExiste.getQuantidade() - 1 <= 0){
            throw new RegradeNegocioException("Quantidade inválida!");
        }
        itemExiste.setQuantidade(itemExiste.getQuantidade() - 1);
        itemExiste.setPrecoTotal(itemExiste.getPrecoUnitario().multiply(BigDecimal.valueOf(itemExiste.getQuantidade())));
        BigDecimal totalAtual = carrinho.getValorTotal() == null
                ? BigDecimal.ZERO
                : carrinho.getValorTotal();

        carrinho.setValorTotal(totalAtual.subtract(produto.getPreco()));
        itemCarrinhoRepository.save(itemExiste);

        return ResponseEntity.ok().body("Quantidade atualizada com sucesso!");
    }
    //----///
    public ResponseEntity<String> removerProduto(ItemCarrinho itemCarrinho, Produto produto, Carrinho carrinho){
        ItemCarrinho itemExiste = itemCarrinhoRepository.findById(itemCarrinho.getIdItem())
                .orElseThrow(() -> new RegradeNegocioException("Esse item não está no carrinho!"));

        if(carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO){
            throw new RegradeNegocioException("Carrinho desativado!");
        }

        carrinho.setValorTotal(carrinho.getValorTotal().subtract(itemCarrinho.getPrecoTotal()));
        itemCarrinhoRepository.delete(itemExiste);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok().body("Produto removido com sucesso!");
    }

}
