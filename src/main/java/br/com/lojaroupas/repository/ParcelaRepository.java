package br.com.lojaroupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import br.com.lojaroupas.model.Parcela;
import br.com.lojaroupas.model.id.ParcelaId;

public interface ParcelaRepository extends JpaRepository<Parcela, ParcelaId>{
    List<Parcela> findByPagamentoCodPagamento(Long codPagamento);
}
