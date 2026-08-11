package com.adps.e_commerce.controller;

import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.*;
import com.adps.e_commerce.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class UsuarioController {

    @Autowired
    private ClienteService clienteService;

    @PatchMapping("/endereco")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(@Valid @RequestBody EnderecoCadastrodTO endereco, @AuthenticationPrincipal Usuario usuarioLogado){
        EnderecoResponseDTO novoEndereco = clienteService.atualizarEndereco(endereco, usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body(novoEndereco);
    }

    @GetMapping("/minhaConta")
    public ClienteResponse2DTO minhaConta(@AuthenticationPrincipal Usuario usuarioLogado){
        return clienteService.minhaConta(usuarioLogado);
    }

    @PatchMapping("/atualizarDados")
    public ResponseEntity<AtualizarUserResponseDTO> atualizarDados(@RequestBody AtualizarClienteDTO atualizar,@AuthenticationPrincipal Usuario usuarioLogado){
        AtualizarUserResponseDTO userAtualizado = clienteService.atualizarDados(atualizar, usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body(userAtualizado);
    }

    @PatchMapping("/desativar")
    public ResponseEntity<String> desativarUser(@AuthenticationPrincipal Usuario usuarioLogado, @RequestBody ConfirmarSenhaIdTO userAndSenha){
       clienteService.desativarUser(userAndSenha.getIdUsuario(), userAndSenha.getSenha(), usuarioLogado);
       return ResponseEntity.status(HttpStatus.OK).body("Cliente desativado com sucesso!");
    }

    @PatchMapping("/ativar")
    public ResponseEntity<String> ativarUser(@AuthenticationPrincipal Usuario usuarioLogado, @RequestBody ConfirmarSenhaIdTO userAndSenha){
        clienteService.ativarUser(userAndSenha.getIdUsuario(), userAndSenha.getSenha(), usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente ativado com sucesso!");
    }

    @DeleteMapping("/apagar")
    public ResponseEntity<String> apagarUser(@AuthenticationPrincipal Usuario usuarioLogado, @RequestBody ConfirmarSenhaIdTO userAndSenha){
        clienteService.deletarCliente(usuarioLogado.getIdUsuario(), userAndSenha.getSenha(), usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente apagado com sucesso!");
    }
}
