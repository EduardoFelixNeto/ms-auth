package br.com.ufpr.tads.dac.msauth.repository;

import br.com.ufpr.tads.dac.msauth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}

