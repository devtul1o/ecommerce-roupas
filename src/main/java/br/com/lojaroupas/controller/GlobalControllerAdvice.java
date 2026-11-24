package br.com.lojaroupas.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.lojaroupas.repository.PessoaRepository;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final PessoaRepository pessoaRepository;

    public GlobalControllerAdvice(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @ModelAttribute("primeiroNome")
    public String adicionarPrimeiroNome(Principal principal) {
        if (principal == null) {
            return null;
        }
        return pessoaRepository.findByEmail(principal.getName())
                .map(pessoa -> {
                    String nome = pessoa.getNomeCompleto().trim();
                    int espaco = nome.indexOf(' ');
                    return (espaco > 0) ? nome.substring(0, espaco) : nome;
                })
                .orElse(null);
    }
}