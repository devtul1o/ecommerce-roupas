package br.com.lojaroupas.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lojaroupas.exception.RegraNegocioException;
import br.com.lojaroupas.model.Compra;
import br.com.lojaroupas.model.Entrega;
import br.com.lojaroupas.model.enums.StatusEntrega;
import br.com.lojaroupas.repository.CompraRepository;
import br.com.lojaroupas.repository.EntregaRepository;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final CompraRepository compraRepository;

    public EntregaService(EntregaRepository entregaRepository, CompraRepository compraRepository) {
        this.entregaRepository = entregaRepository;
        this.compraRepository = compraRepository;
    }

    @Transactional
    public Entrega registrarEntrega(Long codCompra, String codigoRastreio, String transportadora, LocalDate dataPrevista) {
        Compra compra = compraRepository.findById(codCompra)
                .orElseThrow(() -> new RegraNegocioException("Compra não encontrada."));

        if (entregaRepository.findByCompraCodCompra(codCompra).isPresent()) {
            throw new RegraNegocioException("Esta compra já possui uma entrega registrada.");
        }

        Entrega entrega = new Entrega();
        entrega.setCodigoRastreio(codigoRastreio);
        entrega.setTransportadora(transportadora);
        entrega.setStatus(StatusEntrega.ENVIADA);
        entrega.setDataPostagem(LocalDate.now());
        entrega.setDataPrevista(dataPrevista);
        entrega.setCompra(compra);

        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega atualizarStatus(String codigoRastreio, StatusEntrega novoStatus) {
        Entrega entrega = entregaRepository.findById(codigoRastreio)
                .orElseThrow(() -> new RegraNegocioException("Entrega não encontrada."));

        entrega.setStatus(novoStatus);
        if (novoStatus == StatusEntrega.ENTREGUE) {
            entrega.setDataEntrega(LocalDate.now());
        }

        return entregaRepository.save(entrega);
    }
}