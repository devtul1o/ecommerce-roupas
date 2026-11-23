package br.com.lojaroupas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    List<Produto>findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByCategoriasCodCategoria(Long codCategoria);
}
