package com.adps.e_commerce.controller;

import com.adps.e_commerce.service.ClienteService;
import com.adps.e_commerce.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")

public class UsuarioController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AuthenticationManager authentication;

    @Autowired
    private JWTService jwtService;

    @PostMapping("")
}
