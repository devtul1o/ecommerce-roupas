package br.com.lojaroupas.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.lojaroupas.dto.CheckoutDTO;
import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Carrinho;
import br.com.lojaroupas.model.Cliente;
import br.com.lojaroupas.model.Endereco;
import br.com.lojaroupas.model.Pagamento;
import br.com.lojaroupas.model.enums.MetodoPagamento;
import br.com.lojaroupas.repository.EnderecoRepository;
import br.com.lojaroupas.service.CarrinhoService;
import br.com.lojaroupas.service.ClienteService;
import br.com.lojaroupas.service.CompraService;
import br.com.lojaroupas.service.MercadoPagoService;

@Controller
public class CompraController {

    private final CompraService compraService;
    private final CarrinhoService carrinhoService;
    private final ClienteService clienteService;
    private final EnderecoRepository enderecoRepository;
    private final MercadoPagoService mercadoPagoService;

    public CompraController(CompraService compraService,
                            CarrinhoService carrinhoService,
                            ClienteService clienteService,
                            EnderecoRepository enderecoRepository,
                            MercadoPagoService mercadoPagoService) {
        this.compraService = compraService;
        this.carrinhoService = carrinhoService;
        this.clienteService = clienteService;
        this.enderecoRepository = enderecoRepository;
        this.mercadoPagoService = mercadoPagoService;
    }

    @GetMapping("/checkout")
    public String exibirCheckout(Principal principal, Model model) {
        Cliente cliente = clienteService.buscarPorEmail(principal.getName());
        Carrinho carrinho = carrinhoService.buscarPorCliente(cliente);

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("total", carrinhoService.calcularTotal(carrinho));
        model.addAttribute("enderecos", enderecoRepository.findByPessoaCodPessoa(cliente.getCodPessoa()));
        model.addAttribute("metodos", MetodoPagamento.values());
        return "checkout";
    }

    @PostMapping("/checkout/endereco")
    public String salvarEndereco(Principal principal,
                                 @RequestParam String cep,
                                 @RequestParam String logradouro,
                                 @RequestParam String numero,
                                 @RequestParam(required = false) String complemento,
                                 @RequestParam String bairro,
                                 @RequestParam String cidade,
                                 @RequestParam String estado,
                                 RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.buscarPorEmail(principal.getName());

        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setEstado(estado);
        endereco.setPessoa(cliente);
        enderecoRepository.save(endereco);

        redirectAttributes.addFlashAttribute("sucesso", "Endereço cadastrado!");
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/finalizar")
    public String finalizarCompra(Principal principal,
                                  @RequestParam Long codEndereco,
                                  @RequestParam MetodoPagamento metodoPagamento,
                                  @RequestParam(defaultValue = "1") Integer numeroParcelas,
                                  RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorEmail(principal.getName());
            CheckoutDTO dto = new CheckoutDTO(codEndereco, metodoPagamento, numeroParcelas);
            Pagamento pagamento = compraService.finalizarCompra(cliente, dto);

            String urlMercadoPago = mercadoPagoService.criarLinkPagamento(pagamento, metodoPagamento, numeroParcelas);
            return "redirect:" + urlMercadoPago;
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/pagamento/sucesso")
    public String pagamentoSucesso(@RequestParam Long codPagamento, RedirectAttributes redirectAttributes) {
        compraService.aprovarPagamento(codPagamento);
        redirectAttributes.addFlashAttribute("sucesso", "Pagamento aprovado pelo Mercado Pago!");
        return "redirect:/meus-pedidos";
    }

    @GetMapping("/pagamento/pendente")
    public String pagamentoPendente(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("sucesso", "Pagamento pendente de confirmação.");
        return "redirect:/meus-pedidos";
    }

    @GetMapping("/pagamento/falha")
    public String pagamentoFalha(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", "O pagamento não foi aprovado. Tente novamente.");
        return "redirect:/meus-pedidos";
    }

    @GetMapping("/meus-pedidos")
    public String meusPedidos(Principal principal, Model model) {
        Cliente cliente = clienteService.buscarPorEmail(principal.getName());
        model.addAttribute("compras", compraService.listarComprasDoCliente(cliente));
        return "meus-pedidos";
    }

    @GetMapping("/pedidos/{codCompra}/pagar")
    public String retomarPagamento(@PathVariable Long codCompra, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorEmail(principal.getName());
            Pagamento pagamento = compraService.buscarPagamentoPendenteDoCliente(codCompra, cliente);

            int parcelas = (pagamento.getParcelas() != null && !pagamento.getParcelas().isEmpty())
                    ? pagamento.getParcelas().size()
                    : 1;

            // Ajuste "getMetodo()" se o getter na sua entidade Pagamento tiver outro nome
            String urlMercadoPago = mercadoPagoService.criarLinkPagamento(pagamento, pagamento.getMetodo(), parcelas);
            return "redirect:" + urlMercadoPago;
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/meus-pedidos";
        }
    }

    @PostMapping("/pedidos/{codCompra}/cancelar")
    public String cancelarPedido(@PathVariable Long codCompra, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorEmail(principal.getName());
            compraService.cancelarCompra(codCompra, cliente);
            redirectAttributes.addFlashAttribute("sucesso", "Pedido #" + codCompra + " cancelado.");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/meus-pedidos";
    }
}