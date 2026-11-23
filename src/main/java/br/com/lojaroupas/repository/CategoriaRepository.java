package br.com.lojaroupas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    Optional<Categoria> findByNomeIgnoreCase(String nome);

}
