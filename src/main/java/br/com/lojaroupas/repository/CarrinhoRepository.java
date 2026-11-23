package br.com.lojaroupas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Carrinho;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long>{
    Optional<Carrinho> findByClienteCodPessoa(Long codPessoa);
}
