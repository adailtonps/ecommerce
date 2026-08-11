package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Pedido;
import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
    Optional<Pedido> findByIdPedido(String idPedido);
    boolean existsByUsuarioAndStatusPedido(
            Usuario usuario,
            StatusPedido statusPedido
    );
}
