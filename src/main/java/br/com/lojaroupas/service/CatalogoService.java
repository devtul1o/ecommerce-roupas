package br.com.lojaroupas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Categoria;
import br.com.lojaroupas.model.Produto;
import br.com.lojaroupas.model.VariacaoProduto;
import br.com.lojaroupas.repository.CategoriaRepository;
import br.com.lojaroupas.repository.ProdutoRepository;
import br.com.lojaroupas.repository.VariacaoProdutoRepository;

@Service
public class CatalogoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VariacaoProdutoRepository variacaoRepository;

    public CatalogoService(ProdutoRepository produtoRepository,
                           CategoriaRepository categoriaRepository,
                           VariacaoProdutoRepository variacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.variacaoRepository = variacaoRepository;
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Produto> listarPorCategoria(Long codCategoria) {
        return produtoRepository.findByCategoriasCodCategoria(codCategoria);
    }

    public Produto buscarProdutoPorId(Long codProduto) {
        return produtoRepository.findById(codProduto)
                .orElseThrow(() -> new RegraNegocioException("Produto não encontrado."));
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public Categoria salvarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public VariacaoProduto adicionarVariacao(Long codProduto, VariacaoProduto variacao) {
        Produto produto = buscarProdutoPorId(codProduto);
        variacao.setProduto(produto);
        return variacaoRepository.save(variacao);
    }
}