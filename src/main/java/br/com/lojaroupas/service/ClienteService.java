package br.com.lojaroupas.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lojaroupas.dto.CadastroClienteDTO;
import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Carrinho;
import br.com.lojaroupas.model.Cliente;
import br.com.lojaroupas.repository.CarrinhoRepository;
import br.com.lojaroupas.repository.ClienteRepository;
import br.com.lojaroupas.repository.PessoaRepository;

@Service
public class ClienteService {

    private final PessoaRepository pessoaRepository;
    private final ClienteRepository clienteRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(PessoaRepository pessoaRepository,
                          ClienteRepository clienteRepository,
                          CarrinhoRepository carrinhoRepository,
                          PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.clienteRepository = clienteRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente cadastrar(CadastroClienteDTO dto) {
        if (pessoaRepository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("Já existe uma conta cadastrada com este e-mail.");
        }
        if (pessoaRepository.existsByCpf(dto.cpf())) {
            throw new RegraNegocioException("Já existe uma conta cadastrada com este CPF.");
        }

        Cliente cliente = new Cliente();
        cliente.setCpf(dto.cpf());
        cliente.setNomeCompleto(dto.nomeCompleto());
        cliente.setEmail(dto.email());
        cliente.setSenha(passwordEncoder.encode(dto.senha()));
        cliente.setDataNascimento(dto.dataNascimento());
        if (dto.telefone() != null && !dto.telefone().isBlank()) {
            cliente.getTelefones().add(dto.telefone());
        }

        Cliente salvo = clienteRepository.save(cliente);

        Carrinho carrinho = new Carrinho();
        carrinho.setCliente(salvo);
        carrinhoRepository.save(carrinho);

        return salvo;
    }

    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado."));
    }
}