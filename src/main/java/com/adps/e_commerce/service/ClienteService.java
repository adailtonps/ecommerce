package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.*;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.enums.StatusUsuario;
import com.adps.e_commerce.enums.UsuarioRole;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.CarrinhoRepository;
import com.adps.e_commerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CarrinhoRepository carrinhoRepository;


    public UsuarioResponseDTO criarCliente(ClienteCadastroDTO cliente){
        Usuario novoCadastro = new Usuario();

        boolean letraMaiuscula = false;
        boolean digitoEspecial = false;
        boolean numero = false;

        if(cliente.getNomeUsuario() == null || cliente.getNomeUsuario().isBlank()){
            throw new RegradeNegocioException("Digite o nome do cliente!");
        }
        if(cliente.getTelefone() == null || cliente.getTelefone().isBlank() ||
            cliente.getTelefone().length() != 11){
            throw new RegradeNegocioException("Digite o telefone!");
        }
        if(usuarioRepository.existsByTelefone(cliente.getTelefone())){
            throw new RegradeNegocioException("Telefone já cadastrado!");
        }
        if(cliente.getEmail() == null || cliente.getEmail().isBlank() ||
            !cliente.getEmail().contains("@")){
            throw new RegradeNegocioException("Digite o email!");
        }
        if(usuarioRepository.existsByEmail(cliente.getEmail())){
            throw new RegradeNegocioException("Email já cadastrado!");
        }
        if(cliente.getSenha() == null || cliente.getSenha().isBlank() ||
            cliente.getSenha().length() < 8){
            throw new RegradeNegocioException("Digite a senha: PRECISA CONTER, NO MÍNIMO, 8 CARACTERES, "+
                    "UM NÚMERO, UMA LETRA MAIÚSCULA E UM CARÁCTER ESPECIAL (!@#$%%&*?)");
        }
        for (char c : cliente.getSenha().toCharArray()) {
            if(Character.isDigit(c)) numero = true;
            if(Character.isUpperCase(c)) letraMaiuscula = true;
            if("!@#$%&*?".contains(String.valueOf(c))) digitoEspecial = true;
        }
        if(!letraMaiuscula || !digitoEspecial || !numero){
            throw new RegradeNegocioException("Senha inválida! PRECISA CONTER, NO MÍNIMO, 8 CARACTERES, " +
                    "UM NÚMERO, UMA LETRA MAIÚSCULA E UM CARÁCTER ESPECIAL (!@#$%%&*?)");
        }

        novoCadastro.setSenha(encoder.encode(cliente.getSenha()));
        novoCadastro.setStatusUser(StatusUsuario.ATIVADO);
        novoCadastro.setUserRole(UsuarioRole.USER);
        novoCadastro.setNomeUsuario(cliente.getNomeUsuario().trim().toUpperCase());
        novoCadastro.setTelefone(cliente.getTelefone().trim());
        novoCadastro.setEmail(cliente.getEmail().toLowerCase().trim());

        Usuario usuarioSalvo = usuarioRepository.save(novoCadastro);

        Carrinho novoCarrinho = new Carrinho();

        novoCarrinho.setUsuario(novoCadastro);
        novoCarrinho.setValorTotal(BigDecimal.ZERO);
        novoCarrinho.setStatusCarrinho(StatusCarrinho.ATIVADO);
        novoCarrinho.setDataCriacao(LocalDateTime.now());

        carrinhoRepository.save(novoCarrinho);

        return new UsuarioResponseDTO(
                usuarioSalvo.getIdUsuario(),
                usuarioSalvo.getNomeUsuario(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getTelefone(),
                usuarioSalvo.getStatusUser()
        );
    }

    public EnderecoResponseDTO atualizarEndereco(EnderecoCadastrodTO endereco, Usuario usuario){
       if(endereco.getRua() == null || endereco.getNumero() == null || endereco.getBairro() == null ||
               endereco.getCidade() == null || endereco.getEstado() == null ||
               endereco.getCep() == null){
           throw new RegradeNegocioException("Digite o seu endereço completo! (Complemento opcional)");
       }
       if(endereco.getRua() != null && usuario.getRua().isBlank()){
           usuario.setRua(endereco.getRua().trim().toUpperCase());
       }
       if(endereco.getNumero() != null){
           usuario.setNumero(endereco.getNumero());
       }
       if(endereco.getBairro() != null){
           usuario.setBairro(endereco.getBairro().trim().toUpperCase());
       }
       if(endereco.getCidade() != null){
           usuario.setCidade(endereco.getCidade().trim().toUpperCase());
       }
       if(endereco.getEstado() != null){
           usuario.setEstado(endereco.getEstado().trim().toUpperCase());
       }
       if(endereco.getCep() != null){
           usuario.setCep(endereco.getCep());
       }
       if(endereco.getComplemento() != null){
           usuario.setComplemento(endereco.getComplemento().trim().toUpperCase());
       }
       usuarioRepository.save(usuario);
       return new EnderecoResponseDTO(
               "Endereço atualizado com sucesso!\n",
               usuario.getRua(),
               usuario.getNumero(),
               usuario.getBairro(),
               usuario.getCidade(),
               usuario.getEstado(),
               usuario.getCep(),
               usuario.getComplemento()
       );
    }


    public ClienteResponse2DTO minhaConta(Usuario usuario){
        return new ClienteResponse2DTO(
                usuario.getIdUsuario(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getStatusUser()
        );
    }

    public AtualizarUserResponseDTO atualizarDados(AtualizarClienteDTO atualizar, Usuario usuario){
        if(atualizar.getNomeUsuario() != null && !atualizar.getNomeUsuario().isBlank()){
            usuario.setNomeUsuario(atualizar.getNomeUsuario().trim().toUpperCase());
        }
        if(atualizar.getTelefone() != null && !atualizar.getTelefone().isBlank()){
            usuario.setTelefone(atualizar.getTelefone().trim());
        }
        if(atualizar.getEmail() != null && atualizar.getEmail().contains("@") &&
            !atualizar.getEmail().isBlank()){
            usuario.setEmail(atualizar.getEmail().toLowerCase().trim());
        }
        usuarioRepository.save(usuario);

        return new AtualizarUserResponseDTO(
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }


    public ResponseEntity<String> desativarUser(Usuario usuarioLogado, String senha, Carrinho carrinho) {
        if (usuarioLogado.getStatusUser().equals(StatusUsuario.DESATIVADO)) {
            throw new RegradeNegocioException("A conta já está desativada!");
        }
        if(carrinho.getStatusCarrinho().equals(StatusCarrinho.DESATIVADO)) {
            throw new RegradeNegocioException("Carrinho desativado!");
        }
        if (senha == null || senha.isBlank()) {
            throw new RegradeNegocioException("Insira a senha!");
        }
        if (!encoder.matches(senha, usuarioLogado.getSenha())) {
            throw new RegradeNegocioException("Senha incorreta!");
        }
        usuarioLogado.setStatusUser(StatusUsuario.DESATIVADO);
        carrinho.setStatusCarrinho(StatusCarrinho.DESATIVADO);
        return ResponseEntity.ok().body("Conta desativada com sucesso!");
    }

    public ResponseEntity<String> ativarUser(Carrinho carrinho, String senha, Usuario usuarioLogado) {
        if(usuarioLogado.getStatusUser().equals(StatusUsuario.ATIVADO)) {
            throw new RegradeNegocioException("A conta já está ativada!");
        }
        if(senha == null || senha.isBlank()) {
            throw new RegradeNegocioException("Insira a senha!");
        }
        if(!encoder.matches(senha, usuarioLogado.getSenha())) {
            throw new RegradeNegocioException("Senha incorreta!");
        }
        usuarioLogado.setStatusUser(StatusUsuario.ATIVADO);
        carrinho.setStatusCarrinho(StatusCarrinho.ATIVADO);
        return ResponseEntity.ok().body("Conta ativada com sucesso!");
    }

    public ResponseEntity<String> deletarCliente(String senha, Usuario usuarioLogado){
        Usuario existUsuario = usuarioRepository.findByIdUsuario(usuarioLogado.getIdUsuario())
                .orElseThrow(() -> new RegradeNegocioException("Cliente não existe ou já foi apagado!"));

        if(existUsuario.getStatusUser().equals(StatusUsuario.ATIVADO)){
            throw new RegradeNegocioException("Desative a conta primeiro antes de apagar!");
        }

        if(senha == null || senha.isBlank()){
            throw new RegradeNegocioException("Digite a senha!");
        }
        if(!encoder.matches(senha, usuarioLogado.getSenha())){
            throw new RegradeNegocioException("Senha incorreta!");
        }
        if(usuarioLogado.getUserRole().equals(UsuarioRole.ADMIN)){
            throw new RegradeNegocioException("Administradores não podem apagar a própria conta!");
        }

        usuarioRepository.delete(existUsuario);
        return new ResponseEntity<>("Cliente apagado com sucesso!", HttpStatus.OK);
    }
}
