package br.com.lojaroupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import br.com.lojaroupas.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByCompraCodCompra(Long codCompra);
}
