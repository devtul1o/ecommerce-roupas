package br.com.lojaroupas.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.lojaroupas.model.enums.StatusCompra;
import jakarta.persistence.*;

@Entity 
@Table(name = "compra")
public class Compra {

    public Compra() {}

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_compra", nullable = false)
    private Long codCompra;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusCompra status;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "cod_pessoa_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne 
    @JoinColumn(name = "cod_endereco", nullable = false)
    private Endereco endereco;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<ItemCompra> itens = new ArrayList<>();

    @OneToMany(mappedBy = "compra")
    private List<Pagamento> pagamentos = new ArrayList<>();

    @OneToOne(mappedBy = "compra")
    private Entrega entrega;

    public Long getCodCompra() {
        return codCompra;
    }

    public void setCodCompra(Long codCompra) {
        this.codCompra = codCompra;
    }

    public StatusCompra getStatus() {
        return status;
    }

    public void setStatus(StatusCompra status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<ItemCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemCompra> itens) {
        this.itens = itens;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }
    
    
}
