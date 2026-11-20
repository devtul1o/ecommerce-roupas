package br.com.lojaroupas.model.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class ParcelaId implements Serializable {
    private Integer numeroParcela;
    private Long codPagamento;

    public ParcelaId() {}

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Long getCodPagamento() {
        return codPagamento;
    }

    public void setCodPagamento(Long codPagamento) {
        this.codPagamento = codPagamento;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numeroParcela == null) ? 0 : numeroParcela.hashCode());
        result = prime * result + ((codPagamento == null) ? 0 : codPagamento.hashCode());
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
        ParcelaId other = (ParcelaId) obj;
        if (numeroParcela == null) {
            if (other.numeroParcela != null)
                return false;
        } else if (!numeroParcela.equals(other.numeroParcela))
            return false;
        if (codPagamento == null) {
            if (other.codPagamento != null)
                return false;
        } else if (!codPagamento.equals(other.codPagamento))
            return false;
        return true;
    }

    public ParcelaId(Integer numeroParcela, Long codPagamento) {
        this.numeroParcela = numeroParcela;
        this.codPagamento = codPagamento;
    }
}
