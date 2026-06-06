package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Produto;
import com.adps.e_commerce.dto.AdminResponseDTO;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.ClienteCadastroDTO;
import com.adps.e_commerce.dto.UsuarioResponseDTO;
import com.adps.e_commerce.enums.StatusUsuario;
import com.adps.e_commerce.enums.UsuarioRole;
import com.adps.e_commerce.exception.RegradeNegocioException;
import com.adps.e_commerce.repository.CarrinhoRepository;
import com.adps.e_commerce.repository.UsuarioRepository;
import com.adps.e_commerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProdutoRepository produtoRepository;

    public AdminResponseDTO criarAdmin (ClienteCadastroDTO cliente){
        Usuario novoAdmin = new Usuario();

        boolean encontrouNumero = false;
        boolean encontrouLetraMaius = false;
        boolean encontrouSimbolo = false;

        if(cliente.getNomeUsuario() == null || cliente.getNomeUsuario().isBlank()){
            throw new RegradeNegocioException("Nome do Administrador obrigatório!");
        }
        if(cliente.getEmail() == null || cliente.getEmail().isBlank() ||
                cliente.getEmail().contains("@")){
            throw new RegradeNegocioException("Email do Administrador obrigatório!");
        }

        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()){
            throw new RegradeNegocioException("Telefone do Administrador obrigatório!");
        }

        if(cliente.getSenha() == null || cliente.getSenha().isBlank() ||
                cliente.getSenha().length() < 8){
            throw new RegradeNegocioException("Senha obrigatória!");
        }
        for(char c : cliente.getSenha().toCharArray()){
            if(Character.isDigit(c)){encontrouNumero = true;}
            if(Character.isUpperCase(c)){encontrouLetraMaius = true;}
            if("!@#$%&*?".contains(String.valueOf(c))){encontrouSimbolo = true;}
        }
        if(!encontrouNumero && !encontrouLetraMaius && !encontrouSimbolo){
            throw new RegradeNegocioException("Senha inválida! PRECISA CONTER, NO MÍNIMO, 8 CARACTERES, " +
                    "UM NÚMERO, UMA LETRA MAIÚSCULA E UM CARÁCTER ESPECIAL (!@#$%%&*?)");
        }

        novoAdmin.setNomeUsuario(cliente.getNomeUsuario());
        novoAdmin.setEmail(cliente.getEmail());
        novoAdmin.setSenha(encoder.encode(cliente.getSenha()));
        novoAdmin.setEndereco(cliente.getEndereco());
        novoAdmin.setTelefone(cliente.getTelefone());
        novoAdmin.setStatusUser(StatusUsuario.ATIVADO);
        novoAdmin.setUserRole(UsuarioRole.ADMIN);

        Usuario usuarioSalvo = usuarioRepository.save(novoAdmin);

        return new AdminResponseDTO(
                usuarioSalvo.getIdUsuario(),
                usuarioSalvo.getNomeUsuario(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getTelefone(),
                usuarioSalvo.getEndereco(),
                usuarioSalvo.getStatusUser()
        );
    }

    public List<UsuarioResponseDTO> listarClientes(){
        List<Usuario> usuarios = usuarioRepository.findAll();

        if(usuarios.isEmpty()){
            throw new RegradeNegocioException("Nenhum cliente encontrado!");
        }
        return usuarios.stream()
                .map(clientesPresentes -> new UsuarioResponseDTO(
                        clientesPresentes.getIdUsuario(),
                        clientesPresentes.getNomeUsuario(),
                        clientesPresentes.getEmail(),
                        clientesPresentes.getTelefone(),
                        clientesPresentes.getEndereco(),
                        clientesPresentes.getStatusUser()
                )).toList();
    }

    public UsuarioResponseDTO buscarClientePorId(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getEndereco(),
                usuario.getStatusUser()
        );
    }

    public ResponseEntity<String> estoqueAumentar(Produto produto){
        Produto produtoSalvo = produtoRepository.findById(produto.getIdProduto())
                .orElseThrow(() -> new RegradeNegocioException("Produto não encontrado!"));

        produtoSalvo.setQntEstoque(produtoSalvo.getQntEstoque() + 1);

        produtoRepository.save(produtoSalvo);

        return new ResponseEntity<>("Estoque atualizado!", HttpStatus.OK);
    }
}
