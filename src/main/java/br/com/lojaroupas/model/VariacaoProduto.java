package br.com.lojaroupas.model;

import jakarta.persistence.*;

@Entity 
@Table(name = "variacao_produto")
public class VariacaoProduto {

    public VariacaoProduto() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variacao_produto")
    private Long codVariacao;

    @Column(nullable = false, length = 50)
    private String cor;

    @Column(nullable = false, length = 20)
    private String tamanho;

    @Column(nullable = false)
    private Integer estoque;

    @ManyToOne 
    @JoinColumn(name = "cod_produto", nullable = false)
    private Produto produto;

        public Long getCodVariacao() {
        return codVariacao;
    }

    public void setCodVariacao(Long codVariacao) {
        this.codVariacao = codVariacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

}
