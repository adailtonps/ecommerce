package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.EnderecoResponseDTO;
import com.adps.e_commerce.dto.FinalizarCompraDTO;
import com.adps.e_commerce.dto.ItemCarrinhoDTO;
import com.adps.e_commerce.enums.UsuarioRole;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.CarrinhoRepository;
import com.adps.e_commerce.repository.ItemCarrinhoRepository;
import com.adps.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraService {
    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public FinalizarCompraDTO finalizarComprar(Usuario usuario, EnderecoResponseDTO enderecoResponseDTO) {
        Usuario userExiste = usuarioRepository.findByIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RegradeNegocioException("Usuário não encontrado!"));

        if(!userExiste.getUserRole().equals(UsuarioRole.USER)){
            throw new RegradeNegocioException("Somente cliente podem comprar!");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuario(userExiste);

        if(carrinho == null){
            throw new RegradeNegocioException("Carrinho não encontrado!");
        }
        if(carrinho.getItemCarrinho().isEmpty()){
            throw new RegradeNegocioException("Carrinho vazio!");
        }

    }
}
