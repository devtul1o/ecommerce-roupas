package br.com.lojaroupas.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.lojaroupas.model.Funcionario;
import br.com.lojaroupas.repository.FuncionarioRepository;
import br.com.lojaroupas.repository.PessoaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner criarAdminPadrao(PessoaRepository pessoaRepository,
                                       FuncionarioRepository funcionarioRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            String emailAdmin = "admin@lojaroupas.com";
            if (!pessoaRepository.existsByEmail(emailAdmin)) {
                Funcionario admin = new Funcionario();
                admin.setCpf("000.000.000-00");
                admin.setNomeCompleto("Administrador Loja");
                admin.setEmail(emailAdmin);
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setDataNascimento(LocalDate.of(1990, 1, 1));
                admin.setMatricula("FUNC-001");
                admin.setCargo("Gerente");
                admin.setDataAdmissao(LocalDate.now());
                funcionarioRepository.save(admin);
            }
        };
    }
}