package br.com.lojaroupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import br.com.lojaroupas.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long>{
    List<Compra> findByClienteCodPessoa(Long codPessoa);

}
