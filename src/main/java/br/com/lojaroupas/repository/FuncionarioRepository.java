package br.com.lojaroupas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByMatricula(String matricula);
}
