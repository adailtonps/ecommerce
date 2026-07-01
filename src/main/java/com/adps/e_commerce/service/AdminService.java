package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.Produto;
import com.adps.e_commerce.dto.AdminListarUsersDTO;
import com.adps.e_commerce.dto.AdminResponseDTO;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.ClienteCadastroDTO;
import com.adps.e_commerce.dto.ConfirmarSenhaIdTO;
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

        if(cliente.getNome() == null || cliente.getNome().isBlank()){
            throw new RegradeNegocioException("Nome do Administrador obrigatório!");
        }
        if(cliente.getEmail() == null || cliente.getEmail().isBlank() ||
                !cliente.getEmail().contains("@")){
            throw new RegradeNegocioException("Email do Administrador obrigatório!");
        }

        if (cliente.getRua() == null || cliente.getRua().isBlank()){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getNumero() == null){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getBairro() == null || cliente.getBairro().isBlank()){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getCidade() == null || cliente.getCidade().isBlank()){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getEstado() == null || cliente.getEstado().isBlank()){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getCep() == null){
            throw new RegradeNegocioException("Endereço obrigatório!");
        }
        if (cliente.getComplemento() == null || cliente.getComplemento().isBlank()){
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

        novoAdmin.setNome(cliente.getNome().trim().toUpperCase());
        novoAdmin.setEmail(cliente.getEmail().trim().toUpperCase());
        novoAdmin.setSenha(encoder.encode(cliente.getSenha()));
        novoAdmin.setRua(cliente.getRua().trim().toUpperCase());
        novoAdmin.setCep(cliente.getCep());
        novoAdmin.setEstado(cliente.getEstado().trim().toUpperCase());
        novoAdmin.setComplemento(cliente.getComplemento().trim().toUpperCase());
        novoAdmin.setCidade(cliente.getCidade().trim().toUpperCase());
        novoAdmin.setNumero(cliente.getNumero());
        novoAdmin.setBairro(cliente.getBairro().trim().toUpperCase());
        novoAdmin.setTelefone(cliente.getTelefone());
        novoAdmin.setStatusUser(StatusUsuario.ATIVADO);
        novoAdmin.setUserRole(UsuarioRole.ADMIN);

        Usuario usuarioSalvo = usuarioRepository.save(novoAdmin);

        return new AdminResponseDTO(
                usuarioSalvo.getIdUsuario(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getTelefone(),
                usuarioSalvo.getRua(),
                usuarioSalvo.getNumero(),
                usuarioSalvo.getBairro(),
                usuarioSalvo.getCidade(),
                usuarioSalvo.getEstado(),
                usuarioSalvo.getCep(),
                usuarioSalvo.getComplemento(),
                usuarioSalvo.getStatusUser()
        );
    }

    public List<AdminListarUsersDTO> listarClientes(){
        List<Usuario> usuarios = usuarioRepository.findAll();

        if(usuarios.isEmpty()){
            throw new RegradeNegocioException("Nenhum cliente encontrado!");
        }
        return usuarios.stream()
                .map(clientesPresentes -> new AdminListarUsersDTO(
                        clientesPresentes.getIdUsuario(),
                        clientesPresentes.getNome(),
                        clientesPresentes.getEmail(),
                        clientesPresentes.getTelefone(),
                        clientesPresentes.getRua(),
                        clientesPresentes.getNumero(),
                        clientesPresentes.getBairro(),
                        clientesPresentes.getCidade(),
                        clientesPresentes.getEstado(),
                        clientesPresentes.getCep(),
                        clientesPresentes.getComplemento(),
                        clientesPresentes.getStatusUser(),
                        clientesPresentes.getUserRole()
                )).toList();
    }

    //Precisa de revisão
    public AdminListarUsersDTO buscarClientePorId(Usuario usuarioLogado, String idUsuario){
        if(idUsuario.equals(usuarioLogado.getIdUsuario())){
            throw new RegradeNegocioException("Insira um ID diferente do seu!");
        }
        Usuario usuarioExiste = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegradeNegocioException("Usuário não encontrado!"));

        return new AdminListarUsersDTO(
                usuarioExiste.getIdUsuario(),
                usuarioExiste.getNome(),
                usuarioExiste.getEmail(),
                usuarioExiste.getTelefone(),
                usuarioExiste.getRua(),
                usuarioExiste.getNumero(),
                usuarioExiste.getBairro(),
                usuarioExiste.getCidade(),
                usuarioExiste.getEstado(),
                usuarioExiste.getCep(),
                usuarioExiste.getComplemento(),
                usuarioExiste.getStatusUser(),
                usuarioExiste.getUserRole()
        );
    }

    public ResponseEntity<String> estoqueAumentar(Produto produto){
        Produto produtoSalvo = produtoRepository.findById(produto.getIdProduto())
                .orElseThrow(() -> new RegradeNegocioException("Produto não encontrado!"));

        produtoSalvo.setQntEstoque(produtoSalvo.getQntEstoque() + produto.getQntEstoque());
        produtoRepository.save(produtoSalvo);
        return new ResponseEntity<>("Estoque atualizado!", HttpStatus.OK);
    }

    public ResponseEntity<String> estoqueDiminuir(Produto produtoRecebido){
        Produto produtoSalvo = produtoRepository.findById(produtoRecebido.getIdProduto())
                .orElseThrow(() -> new RegradeNegocioException("Produto não encontrado!"));

        produtoSalvo.setQntEstoque(produtoSalvo.getQntEstoque() - produtoRecebido.getQntEstoque());
        produtoRepository.save(produtoSalvo);
        return new ResponseEntity<>("Estoque atualizado!", HttpStatus.OK);
    }
}
