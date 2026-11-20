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
}
