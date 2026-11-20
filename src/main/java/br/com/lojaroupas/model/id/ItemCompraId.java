package br.com.lojaroupas.model.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class ItemCompraId implements Serializable {
    private Long codCompra;
    private Long codVariacao;

    public ItemCompraId() {}

    public ItemCompraId(Long codCompra, Long codVariacao) {
        this.codCompra = codCompra; 
        this.codVariacao = codVariacao;
    }

    public Long getCodCompra() {
        return codCompra;
    }

    public void setCodCompra(Long codCompra) {
        this.codCompra = codCompra;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codCompra == null) ? 0 : codCompra.hashCode());
        result = prime * result + ((codVariacao == null) ? 0 : codVariacao.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemCompraId other = (ItemCompraId) obj;
        if (codCompra == null) {
            if (other.codCompra != null)
                return false;
        } else if (!codCompra.equals(other.codCompra))
            return false;
        if (codVariacao == null) {
            if (other.codVariacao != null)
                return false;
        } else if (!codVariacao.equals(other.codVariacao))
            return false;
        return true;
    }

    public Long getCodVariacao() {
        return codVariacao;
    }

    public void setCodVariacao(Long codVariacao) {
        this.codVariacao = codVariacao;
    }
}
