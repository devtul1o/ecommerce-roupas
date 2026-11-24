package br.com.lojaroupas.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lojaroupas.dto.CheckoutDTO;
import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Carrinho;
import br.com.lojaroupas.model.Cliente;
import br.com.lojaroupas.model.Compra;
import br.com.lojaroupas.model.Endereco;
import br.com.lojaroupas.model.ItemCarrinho;
import br.com.lojaroupas.model.ItemCompra;
import br.com.lojaroupas.model.Pagamento;
import br.com.lojaroupas.model.Parcela;
import br.com.lojaroupas.model.VariacaoProduto;
import br.com.lojaroupas.model.enums.StatusCompra;
import br.com.lojaroupas.model.enums.StatusPagamento;
import br.com.lojaroupas.model.enums.StatusParcela;
import br.com.lojaroupas.model.id.ItemCompraId;
import br.com.lojaroupas.model.id.ParcelaId;
import br.com.lojaroupas.repository.CompraRepository;
import br.com.lojaroupas.repository.EnderecoRepository;
import br.com.lojaroupas.repository.ItemCompraRepository;
import br.com.lojaroupas.repository.PagamentoRepository;
import br.com.lojaroupas.repository.ParcelaRepository;
import br.com.lojaroupas.repository.VariacaoProdutoRepository;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ItemCompraRepository itemCompraRepository;
    private final EnderecoRepository enderecoRepository;
    private final VariacaoProdutoRepository variacaoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ParcelaRepository parcelaRepository;
    private final CarrinhoService carrinhoService;

    public CompraService(CompraRepository compraRepository,
                         ItemCompraRepository itemCompraRepository,
                         EnderecoRepository enderecoRepository,
                         VariacaoProdutoRepository variacaoRepository,
                         PagamentoRepository pagamentoRepository,
                         ParcelaRepository parcelaRepository,
                         CarrinhoService carrinhoService) {
        this.compraRepository = compraRepository;
        this.itemCompraRepository = itemCompraRepository;
        this.enderecoRepository = enderecoRepository;
        this.variacaoRepository = variacaoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.parcelaRepository = parcelaRepository;
        this.carrinhoService = carrinhoService;
    }

    @Transactional
    public Pagamento finalizarCompra(Cliente cliente, CheckoutDTO dto) {
        Carrinho carrinho = carrinhoService.buscarPorCliente(cliente);
        if (carrinho.getItens().isEmpty()) {
            throw new RegraNegocioException("Seu carrinho está vazio.");
        }

        Endereco endereco = enderecoRepository.findById(dto.codEndereco())
                .orElseThrow(() -> new RegraNegocioException("Endereço não encontrado."));

        if (!endereco.getPessoa().getCodPessoa().equals(cliente.getCodPessoa())) {
            throw new RegraNegocioException("O endereço informado não pertence a este cliente.");
        }

        boolean permiteParcelar = dto.metodoPagamento().name().contains("CREDITO");
        int qtdParcelas = (!permiteParcelar || dto.numeroParcelas() == null || dto.numeroParcelas() < 1)
            ? 1
            : dto.numeroParcelas();

        // 1. Valida e baixa o estoque de todas as variações
        for (ItemCarrinho item : carrinho.getItens()) {
            VariacaoProduto variacao = item.getVariacao();
            if (item.getQuantidade() > variacao.getEstoque()) {
                throw new RegraNegocioException("Estoque insuficiente para: " + variacao.getProduto().getNome());
            }
            variacao.setEstoque(variacao.getEstoque() - item.getQuantidade());
            variacaoRepository.save(variacao);        
        }

        // 2. Cria e salva a Compra
        BigDecimal valorTotal = carrinhoService.calcularTotal(carrinho);
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setEndereco(endereco);
        compra.setValorTotal(valorTotal);
        compra.setStatus(StatusCompra.AGUARDANDO_PAGAMENTO);
        Compra compraSalva = compraRepository.save(compra);

        // 3. Congela os itens na tabela compra_contem_variacao
        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setId(new ItemCompraId(compraSalva.getCodCompra(), itemCarrinho.getVariacao().getCodVariacao()));
            itemCompra.setCompra(compraSalva);
            itemCompra.setVariacao(itemCarrinho.getVariacao());
            itemCompra.setQuantidade(itemCarrinho.getQuantidade());
            itemCompra.setPrecoUnitario(itemCarrinho.getVariacao().getProduto().getPreco());
            itemCompraRepository.save(itemCompra);
        }

        // 4. Registra o Pagamento (pendente até confirmação do Mercado Pago Sandbox)
        Pagamento pagamento = new Pagamento();
        pagamento.setCompra(compraSalva);
        pagamento.setMetodo(dto.metodoPagamento());
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setValor(valorTotal);
        pagamento.setDataCriacao(LocalDateTime.now());
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        // 5. Gera as Parcelas
        BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(qtdParcelas), 2, RoundingMode.HALF_UP);
        for (int i = 1; i <= qtdParcelas; i++) {
            Parcela parcela = new Parcela();
            ParcelaId parcelaId = new ParcelaId();
            parcelaId.setCodPagamento(pagamentoSalvo.getCodPagamento());
            parcelaId.setNumeroParcela(i);
            parcela.setId(parcelaId);
            parcela.setPagamento(pagamentoSalvo);
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(LocalDate.now().plusMonths(i));
            parcela.setStatus(StatusParcela.ABERTA);
            parcelaRepository.save(parcela);
        }

        // 6. Limpa o carrinho
        carrinhoService.limparCarrinho(carrinho);

        return pagamentoSalvo;
    }

    @Transactional
    public void aprovarPagamento(Long codPagamento) {
        Pagamento pagamento = pagamentoRepository.findById(codPagamento)
                .orElseThrow(() -> new RegraNegocioException("Pagamento não encontrado."));

        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setDataAprovacao(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        Compra compra = pagamento.getCompra();
        compra.setStatus(StatusCompra.APROVADA);
        compraRepository.save(compra);
    }

    public List<Compra> listarComprasDoCliente(Cliente cliente) {
        return compraRepository.findByClienteCodPessoa(cliente.getCodPessoa());
    }

    public Pagamento buscarPagamentoPendenteDoCliente(Long codCompra, Cliente cliente) {
        Compra compra = compraRepository.findById(codCompra)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado."));

        if (!compra.getCliente().getCodPessoa().equals(cliente.getCodPessoa())) {
            throw new RegraNegocioException("Você não tem permissão para acessar este pedido.");
        }
        if (compra.getStatus() != StatusCompra.AGUARDANDO_PAGAMENTO) {
            throw new RegraNegocioException("Este pedido não está mais aguardando pagamento.");
        }

        return pagamentoRepository.findByCompraCodCompra(codCompra)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RegraNegocioException("Pagamento não encontrado para este pedido."));
    }

    @Transactional
    public void cancelarCompra(Long codCompra, Cliente cliente) {
        Compra compra = compraRepository.findById(codCompra)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado."));

        if (!compra.getCliente().getCodPessoa().equals(cliente.getCodPessoa())) {
            throw new RegraNegocioException("Você não tem permissão para cancelar este pedido.");
        }
        if (compra.getStatus() != StatusCompra.AGUARDANDO_PAGAMENTO) {
            throw new RegraNegocioException("Apenas pedidos aguardando pagamento podem ser cancelados.");
        }

        compra.setStatus(StatusCompra.NEGADA);
        compraRepository.save(compra);
    }
}