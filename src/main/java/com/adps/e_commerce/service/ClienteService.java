package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Carrinho;
import com.adps.e_commerce.domain.Cliente;
import com.adps.e_commerce.dto.ClienteCadastroDTO;
import com.adps.e_commerce.dto.ClienteResponse2DTO;
import com.adps.e_commerce.dto.ClienteResponseDTO;
import com.adps.e_commerce.enums.StatusCarrinho;
import com.adps.e_commerce.enums.StatusUsuario;
import com.adps.e_commerce.enums.UsuarioRole;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder encoder;


    public ClienteResponseDTO criarCliente(ClienteCadastroDTO cliente){
        Cliente novoCadastro = new Cliente();

        boolean letraMaiuscula = false;
        boolean digitoEspecial = false;
        boolean numero = false;

        if(cliente.getNomeCliente() == null || cliente.getNomeCliente().isBlank()){
            throw new RegradeNegocioException("Digite o nome do cliente!");
        }
        if(cliente.getEndereco() == null || cliente.getEndereco().isBlank()){
            throw new RegradeNegocioException("Digite o seu endereço!");
        }
        if(cliente.getTelefone() == null || cliente.getTelefone().isBlank() ||
            cliente.getTelefone().length() != 11){
            throw new RegradeNegocioException("Digite o telefone!");
        }
        if(cliente.getEmail() == null || cliente.getEmail().isBlank() ||
            cliente.getEmail().contains("@")){
            throw new RegradeNegocioException("Digite o email!");
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
        novoCadastro.setNomeCliente(cliente.getNomeCliente());
        novoCadastro.setEndereco(cliente.getEndereco());
        novoCadastro.setTelefone(cliente.getTelefone());
        novoCadastro.setEmail(cliente.getEmail());


        Cliente clienteSalvo = clienteRepository.save(novoCadastro);

        return new ClienteResponseDTO(
                clienteSalvo.getIdCliente(),
                clienteSalvo.getNomeCliente(),
                clienteSalvo.getEmail(),
                clienteSalvo.getTelefone(),
                clienteSalvo.getEndereco(),
                clienteSalvo.getStatusUser(),
                clienteSalvo.getUserRole()
        );
    }
    //Exclusivo para admins
    public List<ClienteResponseDTO> listarClientes(){
        List<Cliente> clientes = clienteRepository.findAll();

        if(clientes.isEmpty()){
            throw new RegradeNegocioException("Nenhum cliente encontrado!");
        }
        return clientes.stream()
                .map(clientesPresentes -> new ClienteResponseDTO(
                        clientesPresentes.getIdCliente(),
                        clientesPresentes.getNomeCliente(),
                        clientesPresentes.getEmail(),
                        clientesPresentes.getTelefone(),
                        clientesPresentes.getEndereco(),
                        clientesPresentes.getStatusUser(),
                        clientesPresentes.getUserRole()
                )).toList();
    }

    //exclusivo para o user
    public ClienteResponse2DTO minhaConta(Cliente cliente){
        return new ClienteResponse2DTO(
                cliente.getIdCliente(),
                cliente.getNomeCliente(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getStatusUser()
        );
    }

    public ResponseEntity<String> desativarUser(Cliente clienteLogado, String senha, Carrinho carrinho) {
        if (clienteLogado.getStatusUser().equals(StatusUsuario.DESATIVADO)) {
            throw new RegradeNegocioException("A conta já está desativada!");
        }
        if(carrinho.getStatusCarrinho().equals(StatusCarrinho.DESATIVADO)) {
            throw new RegradeNegocioException("Carrinho desativado!");
        }
        if (senha == null || senha.isBlank()) {
            throw new RegradeNegocioException("Insira a senha!");
        }
        if (!encoder.matches(senha, clienteLogado.getSenha())) {
            throw new RegradeNegocioException("Senha incorreta!");
        }
        clienteLogado.setStatusUser(StatusUsuario.DESATIVADO);
        carrinho.setStatusCarrinho(StatusCarrinho.DESATIVADO);
        return ResponseEntity.ok().body("Conta desativada com sucesso!");
    }

    public ResponseEntity<String> ativarUser(Carrinho carrinho, String senha, Cliente clienteLogado) {
        if(clienteLogado.getStatusUser().equals(StatusUsuario.ATIVADO)) {
            throw new RegradeNegocioException("A conta já está ativada!");
        }
        if(senha == null || senha.isBlank()) {
            throw new RegradeNegocioException("Insira a senha!");
        }
        if(!encoder.matches(senha, clienteLogado.getSenha())) {
            throw new RegradeNegocioException("Senha incorreta!");
        }
        clienteLogado.setStatusUser(StatusUsuario.ATIVADO);
        carrinho.setStatusCarrinho(StatusCarrinho.ATIVADO);
        return ResponseEntity.ok().body("Conta ativada com sucesso!");
    }

    public ResponseEntity<String> deletarCliente(String senha, Cliente clienteLogado){
        Cliente existCliente = clienteRepository.findByIdCliente(clienteLogado.getIdCliente())
                .orElseThrow(() -> new RegradeNegocioException("Cliente não existe ou já foi apagado!"));

        if(existCliente.getStatusUser().equals(StatusUsuario.ATIVADO)){
            throw new RegradeNegocioException("Desative a conta primeiro antes de apagar!");
        }

        if(senha == null || senha.isBlank()){
            throw new RegradeNegocioException("Digite a senha!");
        }
        if(!encoder.matches(senha, clienteLogado.getSenha())){
            throw new RegradeNegocioException("Senha incorreta!");
        }
        if(clienteLogado.getUserRole().equals(UsuarioRole.ADMIN)){
            throw new RegradeNegocioException("Administradores não podem apagar a própria conta!");
        }

        clienteRepository.delete(existCliente);
        return new ResponseEntity<>("Cliente apagado com sucesso!", HttpStatus.OK);
    }
}
