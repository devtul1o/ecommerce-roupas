package br.com.lojaroupas.model;

import java.math.BigDecimal;

import br.com.lojaroupas.model.id.ItemCompraId;
import jakarta.persistence.*;

@Entity
@Table(name = "compra_contem_variacao")
public class ItemCompra {

    @EmbeddedId
    private ItemCompraId id = new ItemCompraId();

    @ManyToOne
    @MapsId("codCompra")
    @JoinColumn(name = "cod_compra")
    private Compra compra;

    @ManyToOne
    @MapsId("codVariacao")
    @JoinColumn(name = "cod_variacao")
    private VariacaoProduto variacao;

    @Column(name = "preco_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemCompra() {}

    public ItemCompraId getId() {
        return id;
    }

    public void setId(ItemCompraId id) {
        this.id = id;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public VariacaoProduto getVariacao() {
        return variacao;
    }

    public void setVariacao(VariacaoProduto variacao) {
        this.variacao = variacao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}