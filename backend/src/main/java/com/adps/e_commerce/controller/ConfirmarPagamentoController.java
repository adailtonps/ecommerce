package com.adps.e_commerce.controller;

import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.dto.AtualizarStatusPagamentoDTO;
import com.adps.e_commerce.dto.PagamentoResponseDTO;
import com.adps.e_commerce.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class ConfirmarPagamentoController {
    private CompraService  compraService;

    @PostMapping("/pagamento-confirmado")
    public ResponseEntity<Void>confirmarPagamento(
            @RequestBody AtualizarStatusPagamentoDTO pagamento) {

        compraService.confirmarPagamento(pagamento);
        return ResponseEntity.ok().build();
    }
}
