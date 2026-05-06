package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,String> {
    Optional<Cliente> findByIdCliente(String idCliente);
}
