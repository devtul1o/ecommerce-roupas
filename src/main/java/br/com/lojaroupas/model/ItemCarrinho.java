package br.com.lojaroupas.model;

import br.com.lojaroupas.model.id.ItemCarrinhoId;
import jakarta.persistence.*;

@Entity 
@Table(name = "carrinho_possui_variacao")
public class ItemCarrinho {

    public ItemCarrinho() {}

    @EmbeddedId
    private ItemCarrinhoId id = new ItemCarrinhoId();

    @ManyToOne
    @MapsId ("codCarrinho")
    @JoinColumn(name = "cod_carrinho")
    private Carrinho carrinho;

    @ManyToOne 
    @MapsId("codVariacao")
    @JoinColumn(name = "cod_variacao")
    private VariacaoProduto variacao;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemCarrinhoId getId() {
        return id;
    }

    public void setId(ItemCarrinhoId id) {
        this.id = id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public VariacaoProduto getVariacao() {
        return variacao;
    }

    public void setVariacao(VariacaoProduto variacao) {
        this.variacao = variacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
