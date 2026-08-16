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

        System.out.println("Chegou no controller");
        System.out.println("1 - id do pedido"+pagamento.getIdPedido());
        System.out.println("2 - status do pagamento"+pagamento.getStatusPagamento());

        compraService.confirmarPagamento(pagamento);
        System.out.println("Service terminou");
        return ResponseEntity.ok().build();
    }
}
