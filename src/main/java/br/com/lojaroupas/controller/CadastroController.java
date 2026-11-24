package br.com.lojaroupas.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.lojaroupas.dto.CadastroClienteDTO;
import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.service.ClienteService;

@Controller
public class CadastroController {

    private final ClienteService clienteService;

    public CadastroController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/cadastro")
    public String exibirFormulario() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String cpf,
                            @RequestParam String nomeCompleto,
                            @RequestParam String email,
                            @RequestParam String senha,
                            @RequestParam(required = false) String telefone,
                            @RequestParam(required = false) LocalDate dataNascimento,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            CadastroClienteDTO dto = new CadastroClienteDTO(
                    cpf, nomeCompleto, email, senha, telefone, dataNascimento);
            clienteService.cadastrar(dto);
            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RegraNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastro";
        }
    }
}