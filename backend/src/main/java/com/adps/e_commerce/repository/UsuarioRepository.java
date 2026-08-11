package com.adps.e_commerce.repository;

import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,String> {
    Optional<Usuario> findByIdUsuario(String idUsuario);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
}
