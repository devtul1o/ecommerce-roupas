package br.com.lojaroupas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.lojaroupas.service.CatalogoService;

@Controller
public class HomeController {

    private final CatalogoService catalogoService;

    public HomeController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping({"/", "/catalogo"})
    public String index(@RequestParam(required = false) String busca,
                        @RequestParam(required = false) Long categoria,
                        Model model) {

        if (busca != null && !busca.isBlank()) {
            model.addAttribute("produtos", catalogoService.buscarPorNome(busca));
        } else if (categoria != null) {
            model.addAttribute("produtos", catalogoService.listarPorCategoria(categoria));
        } else {
            model.addAttribute("produtos", catalogoService.listarTodosProdutos());
        }

        model.addAttribute("categorias", catalogoService.listarCategorias());
        return "index";
    }

    @GetMapping("/produto/{id}")
    public String detalheProduto(@PathVariable Long id, Model model) {
        model.addAttribute("produto", catalogoService.buscarProdutoPorId(id));
        return "produto-detalhe";
    }

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }
}