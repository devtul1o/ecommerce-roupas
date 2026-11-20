package br.com.lojaroupas.model.id;

import jakarta.persistence.Embeddable;

@Embeddable
public class ItemCarrinhoId  {
    private  Long codCarrinho;
    private Long codVariacao;

    public ItemCarrinhoId() {}

    public ItemCarrinhoId(Long codCarrinho, Long codVariacao) {
        this.codCarrinho = codCarrinho;
        this.codVariacao = codVariacao;
    }

    public Long getCodCarrinho() {
        return codCarrinho;
    }

    public void setCodCarrinho(Long codCarrinho) {
        this.codCarrinho = codCarrinho;
    }

    public Long getCodVariacao() {
        return codVariacao;
    }

    public void setCodVariacao(Long codVariacao) {
        this.codVariacao = codVariacao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codCarrinho == null) ? 0 : codCarrinho.hashCode());
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
        ItemCarrinhoId other = (ItemCarrinhoId) obj;
        if (codCarrinho == null) {
            if (other.codCarrinho != null)
                return false;
        } else if (!codCarrinho.equals(other.codCarrinho))
            return false;
        if (codVariacao == null) {
            if (other.codVariacao != null)
                return false;
        } else if (!codVariacao.equals(other.codVariacao))
            return false;
        return true;
    }
}

