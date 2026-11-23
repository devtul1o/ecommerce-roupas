package br.com.lojaroupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import br.com.lojaroupas.model.Entrega;

public interface EntregaRepository extends JpaRepository<Entrega, String>{
    Optional<Entrega> findByCompraCodCompra(Long codCompra);
}
