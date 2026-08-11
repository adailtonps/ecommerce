package com.adps.e_commerce.controller;

import com.adps.e_commerce.domain.Produto;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.AdminListarUsersDTO;
import com.adps.e_commerce.dto.AdminResponseDTO;
import com.adps.e_commerce.dto.ClienteCadastroDTO;
import com.adps.e_commerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    public AdminService adminService;

    @PostMapping("/criar")
    public ResponseEntity<AdminResponseDTO> criarAdmin(@RequestBody ClienteCadastroDTO cliente){
        AdminResponseDTO novoAdmin = adminService.criarAdmin(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAdmin);
    }

    @GetMapping("/listarUsers")
    public ResponseEntity<List<AdminListarUsersDTO>> listarUsers(@AuthenticationPrincipal Usuario usuarioLogado){
        List<AdminListarUsersDTO> usersPresentes = adminService.listarClientes();
        return ResponseEntity.ok(usersPresentes);
    }

    @PatchMapping("/aumentarEstoque")
    public ResponseEntity<String> aumentarEstoque(@AuthenticationPrincipal Usuario usuarioLogado,
                                                  @RequestBody Produto produto){
        return adminService.estoqueAumentar(produto);
    }

    @PatchMapping("/diminuirEstoque")
    public ResponseEntity<String> diminuirEstoque(@AuthenticationPrincipal Usuario usuarioLogado,
                                                  @RequestBody Produto produto){

        return adminService.estoqueDiminuir(produto);
    }

    @GetMapping("/buscarCliente")
    public ResponseEntity<AdminListarUsersDTO> buscarCliente(@AuthenticationPrincipal Usuario usuarioLogado,
                                                             @RequestBody String idUsuario){
        AdminListarUsersDTO procurarUser = adminService.buscarClientePorId(usuarioLogado, idUsuario);
        return ResponseEntity.ok(procurarUser);
    }
}
