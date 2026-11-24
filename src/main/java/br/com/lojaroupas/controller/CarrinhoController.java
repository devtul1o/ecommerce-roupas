package br.com.lojaroupas.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Carrinho;
import br.com.lojaroupas.model.Cliente;
import br.com.lojaroupas.service.CarrinhoService;
import br.com.lojaroupas.service.ClienteService;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final ClienteService clienteService;

    public CarrinhoController(CarrinhoService carrinhoService, ClienteService clienteService) {
        this.carrinhoService = carrinhoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String verCarrinho(Principal principal, Model model) {
        Cliente cliente = clienteService.buscarPorEmail(principal.getName());
        Carrinho carrinho = carrinhoService.buscarPorCliente(cliente);

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("total", carrinhoService.calcularTotal(carrinho));
        return "carrinho";
    }

    @PostMapping("/adicionar")
    public String adicionarItem(Principal principal,
                                @RequestParam Long codVariacao,
                                @RequestParam Integer quantidade,
                                RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorEmail(principal.getName());
            carrinhoService.adicionarItem(cliente, codVariacao, quantidade);
            redirectAttributes.addFlashAttribute("sucesso", "Item adicionado ao carrinho!");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/carrinho";
    }

    @PostMapping("/remover")
    public String removerItem(Principal principal,
                              @RequestParam Long codVariacao,
                              RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorEmail(principal.getName());
            carrinhoService.removerItem(cliente, codVariacao);
            redirectAttributes.addFlashAttribute("sucesso", "Item removido.");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/carrinho";
    }
}