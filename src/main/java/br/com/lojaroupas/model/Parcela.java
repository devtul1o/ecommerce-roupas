package br.com.lojaroupas.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.lojaroupas.model.enums.StatusParcela;
import br.com.lojaroupas.model.id.ParcelaId;
import jakarta.persistence.*;

@Entity
@Table(name = "parcela")
public class Parcela {

    @EmbeddedId
    private ParcelaId id = new ParcelaId();

    @ManyToOne
    @MapsId("codPagamento")
    @JoinColumn(name = "cod_pagamento")
    private Pagamento pagamento;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusParcela status;

    public Parcela() {}

    public ParcelaId getId() {
        return id;
    }

    public void setId(ParcelaId id) {
        this.id = id;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public StatusParcela getStatus() {
        return status;
    }

    public void setStatus(StatusParcela status) {
        this.status = status;
    }
}