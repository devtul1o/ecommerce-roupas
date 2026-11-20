package br.com.lojaroupas.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity 
@Table(name = "carrinho")
public class Carrinho {

    public Carrinho() {}

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_carrinho", nullable = false)
    private Long codCarrinho;

    @OneToOne 
    @JoinColumn(name = "cod_pessoa_cliente", nullable = false, unique = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Long getCodCarrinho() {
        return codCarrinho;
    }

    public void setCodCarrinho(Long codCarrinho) {
        this.codCarrinho = codCarrinho;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    
}
