package br.com.lojaroupas.dto;

import java.time.LocalDate;

public record CadastroClienteDTO(
    String cpf,
    String nomeCompleto,
    String email,
    String senha,
    String telefone,
    LocalDate dataNascimento
) {}