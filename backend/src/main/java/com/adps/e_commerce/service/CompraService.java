package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.*;
import com.adps.e_commerce.dto.*;
import com.adps.e_commerce.enums.StatusPedido;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.enums.UsuarioRole;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CompraService {
    private final CarrinhoRepository carrinhoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final itemPedidoRepository itemPedidoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;

    @Value("${BANCO_API_KEY}")
    private String bancoApiKey;

    @Transactional
    public FinalizarCompraDTO finalizarComprar(CompraDTO compraDTO,
                                               Usuario userLogado) {

        Usuario userExiste = usuarioRepository.findByIdUsuario(userLogado.getIdUsuario())
                .orElseThrow(() -> new RegradeNegocioException("Usuário não encontrado!"));

        if (!userExiste.getUserRole().equals(UsuarioRole.USER)) {
            throw new RegradeNegocioException("Somente cliente podem comprar!");
        }

        boolean possuiPedidoPendente = pedidoRepository.existsByUsuarioAndStatusPedido(
                userExiste,
                StatusPedido.PENDENTE_PAGAMENTO
        );

        if (possuiPedidoPendente) {
            throw new RegradeNegocioException("Já existe um pedido pendente! Você precisa cancelar ou pagar para fazer outro");
        }

        Pedido pedido = new Pedido();

        Carrinho carrinho = carrinhoRepository.findByUsuario(userExiste);

        if (carrinho.getStatusCarrinho() == StatusCarrinho.DESATIVADO) {
            throw new RegradeNegocioException("Conta desativada: ative a conta para acessar o carrinho!");
        }
        if (carrinho.getItemCarrinho().isEmpty()) {
            throw new RegradeNegocioException("Carrinho vazio!");
        }

        pedido.setUsuario(userExiste);
        pedido.setStatusPedido(StatusPedido.PENDENTE_PAGAMENTO);
        pedido.setCep(userExiste.getCep());
        pedido.setBairro(userExiste.getBairro());
        pedido.setCidade(userExiste.getCidade());
        pedido.setEstado(userExiste.getEstado());
        pedido.setRua(userExiste.getRua());
        pedido.setNumero(userExiste.getNumero());
        pedido.setComplemento(userExiste.getComplemento());
        pedido.setDataPedido(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (String cadaIdItemNoCarrinho : compraDTO.getIdItemCarrinho()) {
            ItemCarrinho itemExisteNoCarrinho = itemCarrinhoRepository.findByIdItemAndCarrinho(
                    cadaIdItemNoCarrinho, carrinho).orElseThrow(
                    () -> new RegradeNegocioException("Esse item não está no carrinho!")
            );

            Produto produtoEmItemCarrinho = itemExisteNoCarrinho.getProduto();

            if (produtoEmItemCarrinho.getQntEstoque() < itemExisteNoCarrinho.getQuantidade()) {
                throw new RegradeNegocioException("Produto sem estoque!");
            }

            itemPedido novoItemPedido = new itemPedido();

            novoItemPedido.setPedido(pedido);

            novoItemPedido.setProduto(produtoEmItemCarrinho);
            novoItemPedido.setQuantidade(itemExisteNoCarrinho.getQuantidade());
            novoItemPedido.setPrecoUnitario(itemExisteNoCarrinho.getPrecoUnitario());
            novoItemPedido.setPrecoTotal(itemExisteNoCarrinho.getPrecoTotal());

            produtoEmItemCarrinho.setQntEstoque(
                    produtoEmItemCarrinho.getQntEstoque() - itemExisteNoCarrinho.getQuantidade()
            );

            total = total.add(itemExisteNoCarrinho.getPrecoTotal());

            itemPedidoRepository.save(novoItemPedido);

            produtoRepository.save(produtoEmItemCarrinho);
        }

        pedido.setValorTotal(total);

        pedidoRepository.save(pedido);

        CriarPagamentoDTO dto = new CriarPagamentoDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setValorTotal(pedido.getValorTotal());

        String apiKey = bancoApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Service-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CriarPagamentoDTO> entity = new HttpEntity<>(dto, headers);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://minisistemabancario.onrender.com/pagamento/gerar";

        ResponseEntity<PagamentoResponseDTO> response =
                restTemplate.postForEntity(
                        url,
                        entity,
                        PagamentoResponseDTO.class
                );

        String codigoPagamento = response.getBody().getCodigoPagamento();

        pedido.setCodigoPagamento(codigoPagamento);

        return new FinalizarCompraDTO(
                "Aguardando o pagamento do pedido...",
                pedido.getIdPedido(),
                pedido.getCodigoPagamento()
        );
    }

    public void confirmarPagamento(AtualizarStatusPagamentoDTO dto) {
        Pedido pedido = pedidoRepository.findByIdPedido(dto.getIdPedido())
                .orElseThrow(() -> new RegradeNegocioException("Pedido não encontrado!"));

        //fazer a verificacao de endereco vazio!!

        if (dto.getStatusPagamento() == StatusPedido.CANCELADO) {
            for (itemPedido itensCancelados : pedido.getItemPedido()) {
                int retornoEstoque = itensCancelados.getProduto().getQntEstoque();
                itensCancelados.getProduto().setQntEstoque(retornoEstoque + itensCancelados.getQuantidade());
                pedido.setStatusPedido(StatusPedido.CANCELADO);
                produtoRepository.save(itensCancelados.getProduto());
            }
        } else if (dto.getStatusPagamento() == StatusPedido.PAGO) {
            pedido.setStatusPedido(StatusPedido.PAGO);
        }
        pedidoRepository.save(pedido);
    }
}

