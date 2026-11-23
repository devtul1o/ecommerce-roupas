package br.com.lojaroupas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lojaroupas.model.Endereco;


public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
    List<Endereco>findByPessoaCodPessoa(Long codPessoa);

}
