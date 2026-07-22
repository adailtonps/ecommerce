package com.adps.e_commerce.controller;

import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.FinalizarCompraDTO;
import com.adps.e_commerce.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compra")
public class CompraController {
    @Autowired
    private CompraService compraService;

    @PostMapping("/finalizarCompra")
    public ResponseEntity<FinalizarCompraDTO> finalizarCompra(@RequestBody Usuario usuario,
                                                             @AuthenticationPrincipal Usuario usuarioLogado){
        FinalizarCompraDTO compra = compraService.finalizarComprar(usuario);
        return ResponseEntity.status(HttpStatus.OK).body(compra);
    }
}
