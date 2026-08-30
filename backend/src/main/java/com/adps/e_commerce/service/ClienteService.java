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

        if(cliente.getNome() == null || cliente.getNome().isBlank()){
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
        novoCadastro.setNome(cliente.getNome().trim().toUpperCase());
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
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getTelefone(),
                usuarioSalvo.getStatusUser()
        );
    }

    public EnderecoResponseDTO atualizarEndereco(EnderecoCadastrodTO endereco, Usuario usuarioLogado){
       if(endereco.getRua() == null || endereco.getNumero() == null || endereco.getBairro() == null ||
               endereco.getCidade() == null || endereco.getEstado() == null ||
               endereco.getCep() == null){
           throw new RegradeNegocioException("Digite o seu endereço completo e corretamente! (Complemento opcional)");
       }
           usuarioLogado.setRua(endereco.getRua().trim().toUpperCase());
           usuarioLogado.setNumero(endereco.getNumero());
           usuarioLogado.setBairro(endereco.getBairro().trim().toUpperCase());
           usuarioLogado.setCidade(endereco.getCidade().trim().toUpperCase());
           usuarioLogado.setEstado(endereco.getEstado().trim().toUpperCase());

           if(endereco.getCep().length() != 8){
               throw new RegradeNegocioException("CEP inválido!");
           }

           usuarioLogado.setCep(endereco.getCep().trim().toUpperCase());

           if(endereco.getComplemento() != null && !endereco.getComplemento().isBlank()){
               usuarioLogado.setComplemento(endereco.getComplemento().trim().toUpperCase());
           }
       usuarioRepository.save(usuarioLogado);
       return new EnderecoResponseDTO(
               "Endereço atualizado com sucesso!",
               usuarioLogado.getRua(),
               usuarioLogado.getNumero(),
               usuarioLogado.getBairro(),
               usuarioLogado.getCidade(),
               usuarioLogado.getEstado(),
               usuarioLogado.getCep(),
               usuarioLogado.getComplemento()
       );
    }


    public ClienteResponse2DTO minhaConta(Usuario usuarioLogado){
        return new ClienteResponse2DTO(
                usuarioLogado.getIdUsuario(),
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                usuarioLogado.getTelefone(),
                usuarioLogado.getRua(),
                usuarioLogado.getNumero(),
                usuarioLogado.getBairro(),
                usuarioLogado.getCidade(),
                usuarioLogado.getEstado(),
                usuarioLogado.getCep(),
                usuarioLogado.getComplemento(),
                usuarioLogado.getStatusUser()
        );
    }

    public AtualizarUserResponseDTO atualizarDados(AtualizarClienteDTO atualizar, Usuario usuarioLogado){
        if(atualizar.getNome() != null && !atualizar.getNome().isBlank()){
            usuarioLogado.setNome(atualizar.getNome().trim().toUpperCase());
        }
        if(atualizar.getTelefone() != null && !atualizar.getTelefone().isBlank()){
            usuarioLogado.setTelefone(atualizar.getTelefone().trim());
        }
        if(atualizar.getEmail() != null && atualizar.getEmail().contains("@") &&
            !atualizar.getEmail().isBlank()){
            usuarioLogado.setEmail(atualizar.getEmail().toLowerCase().trim());
        }
        usuarioRepository.save(usuarioLogado);

        return new AtualizarUserResponseDTO(
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                usuarioLogado.getTelefone()
        );
    }


    public void desativarUser(String idUsuario, String senha, Usuario usuarioLogado) {
        Carrinho carrinho = carrinhoRepository.findByUsuario(usuarioLogado);
        if(carrinho == null){
            throw new RegradeNegocioException("Usuário não possui carrinho");
        }
        if(idUsuario == null || !usuarioLogado.getIdUsuario().equals(idUsuario)){
            throw new RegradeNegocioException("Você só pode desativar a sua conta!");
        }
        if (usuarioLogado.getStatusUser().equals(StatusUsuario.DESATIVADO)) {
            throw new RegradeNegocioException("A conta já está desativada!");
        }
        if (senha == null || senha.isBlank()) {
            throw new RegradeNegocioException("Insira a senha!");
        }
        if (!encoder.matches(senha, usuarioLogado.getSenha())) {
            throw new RegradeNegocioException("Senha incorreta!");
        }
        usuarioLogado.setStatusUser(StatusUsuario.DESATIVADO);
        carrinho.setStatusCarrinho(StatusCarrinho.DESATIVADO);

        usuarioRepository.save(usuarioLogado);
        carrinhoRepository.save(carrinho);
    }


    public void ativarUser(String idUsuario, String senha, Usuario usuarioLogado) {
        Carrinho carrinho = carrinhoRepository.findByUsuario(usuarioLogado);
        if(carrinho == null){
            throw new RegradeNegocioException("Usuário não possui carrinho");
        }
        if(idUsuario == null || !usuarioLogado.getIdUsuario().equals(idUsuario)){
            throw new RegradeNegocioException("Você só pode ativar a sua conta!");
        }
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

        usuarioRepository.save(usuarioLogado);
        carrinhoRepository.save(carrinho);
    }

    public void deletarCliente(String idUsuario, String senha, Usuario usuarioLogado){
        Usuario existUsuario = usuarioRepository.findByIdUsuario(usuarioLogado.getIdUsuario())
                .orElseThrow(() -> new RegradeNegocioException("Cliente não existe ou já foi apagado!"));

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuarioLogado);

        if(carrinho == null){
            throw new RegradeNegocioException("Carrinho de usuário não encontrado!");
        }

        if(existUsuario.getStatusUser().equals(StatusUsuario.ATIVADO)){
            throw new RegradeNegocioException("Desative a conta primeiro antes de apagar!");
        }
        if(!existUsuario.getIdUsuario().equals(idUsuario)){
            throw new RegradeNegocioException("Você só pode apagar a sua conta!");
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
        carrinhoRepository.delete(carrinho);
        usuarioRepository.delete(existUsuario);
    }
}
