package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Pedido;
import com.adps.e_commerce.dto.ItemPedidoResponseDTO;
import com.adps.e_commerce.dto.PedidoResponseDTO;
import com.adps.e_commerce.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public PedidoResponseDTO pedidoUsuario(Pedido pedido){
        List<ItemPedidoResponseDTO> itens =
                pedido.getItemPedido()
                        .stream()
                        .map(
                                itemPedido -> new ItemPedidoResponseDTO(
                                        itemPedido.getProduto().getIdProduto(),
                                        itemPedido.getProduto().getNome(),
                                        itemPedido.getQuantidade(),
                                        itemPedido.getPrecoUnitario(),
                                        itemPedido.getPrecoTotal()
                                )
                        ).toList();

        return new PedidoResponseDTO(
                pedido.getIdPedido(),
                pedido.getValorTotal(),
                pedido.getStatusPedido(),
                pedido.getDataPagamento(),
                pedido.getDataPedido(),
                pedido.getRua(),
                pedido.getNumero(),
                pedido.getBairro(),
                pedido.getCidade(),
                pedido.getEstado(),
                pedido.getCep(),
                pedido.getComplemento(),
                itens
        );
    }

}
