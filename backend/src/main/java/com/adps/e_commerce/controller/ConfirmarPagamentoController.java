package com.adps.e_commerce.controller;

import com.adps.e_commerce.dto.AtualizarStatusPagamentoDTO;
import com.adps.e_commerce.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class ConfirmarPagamentoController {
    private final CompraService  compraService;

    @PostMapping("/pagamento-confirmado")
    public ResponseEntity<Void>confirmarPagamento(
            @RequestBody AtualizarStatusPagamentoDTO pagamento) {

        compraService.confirmarPagamento(pagamento);
        return ResponseEntity.ok().build();
    }
}
