package br.com.lojaroupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.VariacaoProduto;
import java.util.List;

public interface VariacaoProdutoRepository extends JpaRepository<VariacaoProduto, Long>{
    List<VariacaoProduto> findByProdutoCodProduto(Long codProduto);

}
