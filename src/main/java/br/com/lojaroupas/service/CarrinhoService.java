package br.com.lojaroupas.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Carrinho;
import br.com.lojaroupas.model.Cliente;
import br.com.lojaroupas.model.ItemCarrinho;
import br.com.lojaroupas.model.VariacaoProduto;
import br.com.lojaroupas.model.id.ItemCarrinhoId;
import br.com.lojaroupas.repository.CarrinhoRepository;
import br.com.lojaroupas.repository.ItemCarrinhoRepository;
import br.com.lojaroupas.repository.VariacaoProdutoRepository;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final VariacaoProdutoRepository variacaoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ItemCarrinhoRepository itemCarrinhoRepository,
                           VariacaoProdutoRepository variacaoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.itemCarrinhoRepository = itemCarrinhoRepository;
        this.variacaoRepository = variacaoRepository;
    }

    public Carrinho buscarPorCliente(Cliente cliente) {
        return carrinhoRepository.findByClienteCodPessoa(cliente.getCodPessoa())
                .orElseThrow(() -> new RegraNegocioException("Carrinho não encontrado para este cliente."));
    }

    @Transactional
    public void adicionarItem(Cliente cliente, Long codVariacao, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero.");
        }

        Carrinho carrinho = buscarPorCliente(cliente);
        VariacaoProduto variacao = variacaoRepository.findById(codVariacao)
                .orElseThrow(() -> new RegraNegocioException("Variação de produto não encontrada."));

        ItemCarrinhoId itemId = new ItemCarrinhoId(carrinho.getCodCarrinho(), variacao.getCodVariacao());
        Optional<ItemCarrinho> existente = itemCarrinhoRepository.findById(itemId);

        int novaQuantidade = existente.map(item -> item.getQuantidade() + quantidade).orElse(quantidade);

        if (novaQuantidade > variacao.getEstoque()) {
            throw new RegraNegocioException("Estoque insuficiente. Disponível: " + variacao.getEstoque());
        }

        if (existente.isPresent()) {
            ItemCarrinho item = existente.get();
            item.setQuantidade(novaQuantidade);
            itemCarrinhoRepository.save(item);
        } else {
            ItemCarrinho novoItem = new ItemCarrinho();
            novoItem.setId(itemId);
            novoItem.setCarrinho(carrinho);
            novoItem.setVariacao(variacao);
            novoItem.setQuantidade(novaQuantidade);
            itemCarrinhoRepository.save(novoItem);
        }
    }

    @Transactional
    public void removerItem(Cliente cliente, Long codVariacao) {
        Carrinho carrinho = buscarPorCliente(cliente);
        ItemCarrinhoId itemId = new ItemCarrinhoId(carrinho.getCodCarrinho(), codVariacao);

        if (!itemCarrinhoRepository.existsById(itemId)) {
            throw new RegraNegocioException("Item não encontrado no carrinho.");
        }
        itemCarrinhoRepository.deleteById(itemId);
    }

    @Transactional
    public void limparCarrinho(Carrinho carrinho) {
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
    }

public BigDecimal calcularTotal(Carrinho carrinho) {
    return carrinho.getItens().stream()
            .map(item -> item.getVariacao().getProduto().getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
}
}