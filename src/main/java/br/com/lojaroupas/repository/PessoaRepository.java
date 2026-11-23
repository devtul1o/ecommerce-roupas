package br.com.lojaroupas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}