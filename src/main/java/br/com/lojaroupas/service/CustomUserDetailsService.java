package br.com.lojaroupas.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lojaroupas.model.Funcionario;
import br.com.lojaroupas.model.Pessoa;
import br.com.lojaroupas.repository.PessoaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PessoaRepository pessoaRepository;

    public CustomUserDetailsService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Pessoa pessoa = pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        String role = (pessoa instanceof Funcionario) ? "FUNCIONARIO" : "CLIENTE";

        return User.builder()
                .username(pessoa.getEmail())
                .password(pessoa.getSenha())
                .roles(role)
                .build();
    }
}