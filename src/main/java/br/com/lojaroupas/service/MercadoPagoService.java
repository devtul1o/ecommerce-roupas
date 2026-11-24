package br.com.lojaroupas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferencePaymentTypeRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

import br.com.lojaroupas.model.Pagamento;
import br.com.lojaroupas.model.enums.MetodoPagamento;

@Service
public class MercadoPagoService {

    @Value("${mp.access-token}")
    private String accessToken;

    @Value("${app.base-url}")
    private String appBaseUrl;

    public String criarLinkPagamento(Pagamento pagamento, MetodoPagamento metodoPagamento, int numeroParcelas) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(pagamento.getCodPagamento().toString())
                    .title("Pedido #" + pagamento.getCompra().getCodCompra() + " - Loja de Roupas")
                    .quantity(1)
                    .currencyId("BRL")
                    .unitPrice(pagamento.getValor())
                    .build();

            String baseUrl = appBaseUrl + "/pagamento";
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(baseUrl + "/sucesso?codPagamento=" + pagamento.getCodPagamento())
                    .failure(baseUrl + "/falha?codPagamento=" + pagamento.getCodPagamento())
                    .pending(baseUrl + "/pendente?codPagamento=" + pagamento.getCodPagamento())
                    .build();

            PreferencePaymentMethodsRequest paymentMethods = configurarMetodosPagamento(metodoPagamento, numeroParcelas);

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .paymentMethods(paymentMethods)
                    .externalReference(pagamento.getCodPagamento().toString())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            return preference.getSandboxInitPoint();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento no Mercado Pago: " + e.getMessage(), e);
        }
}

private PreferencePaymentMethodsRequest configurarMetodosPagamento(MetodoPagamento metodoPagamento, int numeroParcelas) {
    String metodo = metodoPagamento.name().toUpperCase();
    int maxParcelas = numeroParcelas > 0 ? numeroParcelas : 12;

    List<PreferencePaymentTypeRequest> tiposExcluidos = new ArrayList<>();

    if (metodo.contains("CREDITO")) {
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("ticket").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("debit_card").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("prepaid_card").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("account_money").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("digital_currency").build());

        return PreferencePaymentMethodsRequest.builder()
                .excludedPaymentTypes(tiposExcluidos)
                .installments(maxParcelas) // permite selecionar de 1x até maxParcelas
                .build();
    }

    if (metodo.contains("DEBITO")) {
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("credit_card").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("ticket").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("account_money").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("digital_currency").build());

        return PreferencePaymentMethodsRequest.builder()
                .excludedPaymentTypes(tiposExcluidos)
                .installments(1)
                .build();
    }

    if (metodo.contains("BOLETO")) {
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("credit_card").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("debit_card").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("prepaid_card").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("account_money").build());
        tiposExcluidos.add(PreferencePaymentTypeRequest.builder().id("digital_currency").build());

        return PreferencePaymentMethodsRequest.builder()
                .excludedPaymentTypes(tiposExcluidos)
                .installments(1)
                .build();
    }

    return PreferencePaymentMethodsRequest.builder()
            .installments(1)
            .build();
}
}