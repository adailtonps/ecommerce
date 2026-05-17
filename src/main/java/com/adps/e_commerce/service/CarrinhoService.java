package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.*;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class CarrinhoService {
    @Autowired
    private ItemCarrinhoRepository itemCarrinhoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Carrinho buscarCarrinho(Cliente cliente) {
        Optional<Carrinho> carrinhoExistente = carrinhoRepository.findByClienteAndStatusCarrinho(cliente, StatusCarrinho.ATIVADO);
        if(carrinhoExistente.isPresent()) {
            return carrinhoExistente.get();
        } else {
            throw new RegradeNegocioException("Conta desativada!");
        }
    }

    public ResponseEntity<String> adicionarProduto(Cliente cliente, Produto produto) {
        if(produto.getQuantidade() == 0){
            throw new RegradeNegocioException("Esse produto não está disponível!");
        }

        Carrinho carrinho = buscarCarrinho(cliente);

        ItemCarrinho itemExiste = itemCarrinhoRepository.findByCarrinhoAndProduto(carrinho, produto);

        if(itemExiste != null){
            throw new RegradeNegocioException("Produto já adicionado no carrinho!");
        }

        ItemCarrinho novoItem = new ItemCarrinho();

        novoItem.setProduto(produto);
        novoItem.setCarrinho(carrinho);
        novoItem.setQuantidade(1);
        novoItem.setPrecoUnitario(produto.getPreco());


        itemCarrinhoRepository.save(novoItem);

        return ResponseEntity.ok().body("Produto adicionado com sucesso!");
    }

    public ResponseEntity<String> aumentarQuantidade(ItemCarrinho itemCarrinho, Produto produto, Carrinho carrinho, Cliente cliente) {
        ItemCarrinho itemExiste = itemCarrinhoRepository.findById(itemCarrinho.getIdItem())
                .orElseThrow(() -> new RegradeNegocioException("Esse item não está no carrinho!"));

        if(itemExiste.getQuantidade() + 1 > produto.getQuantidade()){
            throw new RegradeNegocioException("Quantidade indisponível!");
        }

        if(carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO){
            throw new RegradeNegocioException("Carrinho desativado!");
        }

        if(!itemExiste.getCarrinho().getCliente().equals(cliente)){
            throw new RegradeNegocioException("Carrinho não pertence ao cliente!");
        }

        itemExiste.setQuantidade(itemExiste.getQuantidade() + 1);
        BigDecimal qntConvertida= (BigDecimal.valueOf(itemExiste.getQuantidade())) ;
        itemExiste.setPrecoTotal(produto.getPreco().multiply(qntConvertida));

        itemCarrinhoRepository.save(itemExiste);

        return ResponseEntity.ok().body("Quantidade atualizada com sucesso!");
    }

    public ResponseEntity<String> diminuirQuantidade(ItemCarrinho itemCarrinho, Produto produto, Cliente clienteLogado, Carrinho carrinho){
        ItemCarrinho itemExiste = itemCarrinhoRepository.findById(itemCarrinho.getIdItem())
                .orElseThrow(() -> new RegradeNegocioException("Esse item não está no carrinho!"));

        if(carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO){
            throw new RegradeNegocioException("Carrinho desativado!");
        }

        if(!itemExiste.getCarrinho().getCliente().equals(clienteLogado)){
            throw new RegradeNegocioException("Carrinho não pertence ao cliente!");
        }

        if(itemExiste.getQuantidade() - 1 <= 0){
            throw new RegradeNegocioException("Quantidade inválida!");
        }
        itemExiste.setQuantidade(itemExiste.getQuantidade() - 1);
        itemExiste.setPrecoTotal(itemExiste.getPrecoTotal().subtract(itemExiste.getPrecoUnitario()));
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

        itemCarrinhoRepository.delete(itemExiste);

        return ResponseEntity.ok().body("Produto removido com sucesso!");
    }

}
