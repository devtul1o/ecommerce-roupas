package br.com.lojaroupas.model;

import java.time.LocalDate;

import br.com.lojaroupas.model.enums.StatusEntrega;
import jakarta.persistence.*;

@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @Column(name = "codigo_rastreio", length = 50)
    private String codigoRastreio;

    @Column(nullable = false, length = 100)
    private String transportadora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusEntrega status;

    @Column(name = "data_postagem")
    private LocalDate dataPostagem;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @OneToOne
    @JoinColumn(name = "cod_compra", nullable = false, unique = true)
    private Compra compra;

    public Entrega() {}

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

    public LocalDate getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(LocalDate dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
}