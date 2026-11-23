package br.com.lojaroupas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    Optional<Cliente> findByEmail(String email);
}
