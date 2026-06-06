package com.adps.e_commerce.controller;

import com.adps.e_commerce.dto.ClienteCadastroDTO;
import com.adps.e_commerce.dto.LoginDTO;
import com.adps.e_commerce.dto.LoginResponseDTO;
import com.adps.e_commerce.dto.UsuarioResponseDTO;
import com.adps.e_commerce.service.ClienteService;
import com.adps.e_commerce.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    ClienteService clienteService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @PostMapping("/cliente")
    public ResponseEntity<UsuarioResponseDTO> salvarCliente(@RequestBody ClienteCadastroDTO cliente){
        UsuarioResponseDTO novoUser = clienteService.criarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody LoginDTO login){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getSenha()
                )
        );
        String token = jwtService.gerarToken(authentication);
        return ResponseEntity.ok(new LoginResponseDTO("Login realizado com sucesso!",token));
    }
}
