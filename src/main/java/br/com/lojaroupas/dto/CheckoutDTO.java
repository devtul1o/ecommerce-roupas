package br.com.lojaroupas.dto;

import br.com.lojaroupas.model.enums.MetodoPagamento;

public record CheckoutDTO(
    Long codEndereco,
    MetodoPagamento metodoPagamento,
    Integer numeroParcelas
) {}