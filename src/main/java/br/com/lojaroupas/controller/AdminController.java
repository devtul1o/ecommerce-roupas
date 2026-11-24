package br.com.lojaroupas.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Categoria;
import br.com.lojaroupas.model.Produto;
import br.com.lojaroupas.model.VariacaoProduto;
import br.com.lojaroupas.model.enums.StatusEntrega;
import br.com.lojaroupas.repository.CategoriaRepository;
import br.com.lojaroupas.repository.CompraRepository;
import br.com.lojaroupas.service.CatalogoService;
import br.com.lojaroupas.service.EntregaService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CatalogoService catalogoService;
    private final EntregaService entregaService;
    private final CategoriaRepository categoriaRepository;
    private final CompraRepository compraRepository;

    public AdminController(CatalogoService catalogoService,
                           EntregaService entregaService,
                           CategoriaRepository categoriaRepository,
                           CompraRepository compraRepository) {
        this.catalogoService = catalogoService;
        this.entregaService = entregaService;
        this.categoriaRepository = categoriaRepository;
        this.compraRepository = compraRepository;
    }

    @GetMapping
    public String painel(Model model) {
        model.addAttribute("categorias", catalogoService.listarCategorias());
        model.addAttribute("produtos", catalogoService.listarTodosProdutos());
        model.addAttribute("compras", compraRepository.findAll());
        model.addAttribute("statusEntregas", StatusEntrega.values());
        return "admin";
    }

    @PostMapping("/categorias")
    public String salvarCategoria(@RequestParam String nome,
                              @RequestParam String descricao,
                              RedirectAttributes redirectAttributes) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        catalogoService.salvarCategoria(categoria);
        redirectAttributes.addFlashAttribute("sucesso", "Categoria cadastrada!");
        return "redirect:/admin";
}

    @PostMapping("/produtos")
        public String salvarProduto(@RequestParam String nome,
                            @RequestParam String descricao,
                            @RequestParam String marca,
                            @RequestParam String imagem,
                            @RequestParam BigDecimal preco,
                            @RequestParam(required = false) Long codCategoria,
                            RedirectAttributes redirectAttributes) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setMarca(marca);
        produto.setImagem(imagem);
        produto.setPreco(preco);

        if (codCategoria != null) {
            categoriaRepository.findById(codCategoria)
                    .ifPresent(cat -> produto.getCategorias().add(cat));
        }

        catalogoService.salvarProduto(produto);
        redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado!");
        return "redirect:/admin";
    }

    @PostMapping("/variacoes")
    public String adicionarVariacao(@RequestParam Long codProduto,
                                    @RequestParam String tamanho,
                                    @RequestParam String cor,
                                    @RequestParam Integer estoque,
                                    RedirectAttributes redirectAttributes) {
        try {
            VariacaoProduto variacao = new VariacaoProduto();
            variacao.setTamanho(tamanho);
            variacao.setCor(cor);
            variacao.setEstoque(estoque);
            catalogoService.adicionarVariacao(codProduto, variacao);
            redirectAttributes.addFlashAttribute("sucesso", "Variação adicionada ao produto!");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/entregas")
    public String registrarEntrega(@RequestParam Long codCompra,
                                   @RequestParam String codigoRastreio,
                                   @RequestParam String transportadora,
                                   @RequestParam LocalDate dataPrevista,
                                   RedirectAttributes redirectAttributes) {
        try {
            entregaService.registrarEntrega(codCompra, codigoRastreio, transportadora, dataPrevista);
            redirectAttributes.addFlashAttribute("sucesso", "Entrega registrada com sucesso!");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/entregas/status")
    public String atualizarStatusEntrega(@RequestParam String codigoRastreio,
                                         @RequestParam StatusEntrega status,
                                         RedirectAttributes redirectAttributes) {
        try {
            entregaService.atualizarStatus(codigoRastreio, status);
            redirectAttributes.addFlashAttribute("sucesso", "Status da entrega atualizado!");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin";
    }
}