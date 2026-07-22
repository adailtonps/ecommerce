package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.*;
import com.adps.e_commerce.dto.FinalizarCompraDTO;
import com.adps.e_commerce.enums.PedidoStatus;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.enums.UsuarioRole;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraService {
    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private itemPedidoRepository itemPedidoRepository;

    @Transactional
    public FinalizarCompraDTO finalizarComprar(Usuario usuario) {
        Usuario userExiste = usuarioRepository.findByIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RegradeNegocioException("Usuário não encontrado!"));

        if(!userExiste.getUserRole().equals(UsuarioRole.USER)){
            throw new RegradeNegocioException("Somente cliente podem comprar!");
        }
        Pedido pedido = new Pedido();

        Carrinho carrinho = carrinhoRepository.findByUsuario(userExiste);

        if(carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO){
            throw new RegradeNegocioException("Conta desativada: ative a conta para acessar o carrinho!");
        }
        if(carrinho.getItemCarrinho().isEmpty()){
            throw new RegradeNegocioException("Carrinho vazio!");
        }
        pedido.setUsuario(userExiste);
        pedido.setValorTotal(carrinho.getValorTotal());
        pedido.setPedidoStatus(PedidoStatus.PENDENTE_PAGAMENTO);
        pedido.setCep(userExiste.getCep());
        pedido.setBairro(userExiste.getBairro());
        pedido.setCidade(userExiste.getCidade());
        pedido.setEstado(userExiste.getEstado());
        pedido.setRua(userExiste.getRua());
        pedido.setNumero(userExiste.getNumero());
        pedido.setComplemento(userExiste.getComplemento());

        pedidoRepository.save(pedido);

        for(ItemCarrinho listaItemCarrinho : carrinho.getItemCarrinho()) {
            itemPedido itemPedido = new itemPedido();
            Produto produto = listaItemCarrinho.getProduto();

            if (produto.getQntEstoque() < listaItemCarrinho.getQuantidade()){
                throw new RegradeNegocioException("Produto sem estoque!");
            }

            itemPedido.setPedido(pedido);

            itemPedido.setProduto(listaItemCarrinho.getProduto());
            itemPedido.setQuantidade(listaItemCarrinho.getQuantidade());
            itemPedido.setPrecoUnitario(listaItemCarrinho.getPrecoUnitario());
            itemPedido.setPrecoTotal(listaItemCarrinho.getPrecoTotal());

            produto.setQntEstoque(
                    produto.getQntEstoque() - listaItemCarrinho.getQuantidade()
            );

            itemPedidoRepository.save(itemPedido);
            produtoRepository.save(produto);

        }

        return new FinalizarCompraDTO(
                "Aguardando o pagamento do pedido...",
                pedido
        );
    }
}
