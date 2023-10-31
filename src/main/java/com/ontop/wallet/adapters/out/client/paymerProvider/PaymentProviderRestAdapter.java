package com.ontop.wallet.adapters.out.client.paymerProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.wallet.adapters.in.resource.exception.ServiceApiException;
import com.ontop.wallet.adapters.out.client.RestClient;
import com.ontop.wallet.adapters.out.client.paymerProvider.request.*;
import com.ontop.wallet.adapters.out.client.paymerProvider.response.PaymentProviderResponse;
import com.ontop.wallet.application.core.domain.ProviderTransactionRequest;
import com.ontop.wallet.application.core.domain.ProviderTransactionResponse;
import com.ontop.wallet.application.ports.out.rest.PaymentProviderRestAdapterPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Currency;

@Slf4j
@Component
public class PaymentProviderRestAdapter extends RestClient
        implements PaymentProviderRestAdapterPort {

    private String paymentProviderUrl;
    private String onTopName;
    private String onTopAccountNumber;
    private String onTopRoutingNumber;
    private String onTopAccountType;
    private String onTopAccountCurrency;

    public PaymentProviderRestAdapter(RestTemplate paymentProviderRestTemplate,
                                      @Value("${url.payment-provider.payment}") String paymentProviderUrl,
                                      @Value("${ontop.name}") String onTopName,
                                      @Value("${ontop.account.number}") String onTopAccountNumber,
                                      @Value("${ontop.routing.number}") String onTopRoutingNumber,
                                      @Value("${ontop.bank-account.type}") String onTopAccountType,
                                      @Value("${ontop.account.currency}") String onTopAccountCurrency) {
        super(paymentProviderRestTemplate);
        objectMapper = new ObjectMapper();
        this.onTopName = onTopName;
        this.onTopAccountNumber = onTopAccountNumber;
        this.onTopRoutingNumber = onTopRoutingNumber;
        this.onTopAccountType = onTopAccountType;
        this.paymentProviderUrl = paymentProviderUrl;
        this.onTopAccountCurrency = onTopAccountCurrency;
    }

    @Override
    public ProviderTransactionResponse createPaymentTransaction(
            ProviderTransactionRequest providerTransaction) {
        PaymentProviderRequest paymentProviderRequest = PaymentProviderRequest.builder()
                .paymentProviderSourceRequest(PaymentProviderSourceRequest.builder()
                        .type(onTopAccountType)
                        .paymentProviderSourceInformationRequest(
                                PaymentProviderSourceInformationRequest.builder()
                                        .name(onTopName).build())
                        .paymentProviderAccountRequest(
                                PaymentProviderAccountRequest.builder()
                                        .accountNumber(onTopAccountNumber)
                                        .currency(Currency.getInstance(onTopAccountCurrency))
                                        .routingNumber(onTopRoutingNumber)
                                        .build())
                        .build())
                .paymentProviderDestinationRequest(PaymentProviderDestinationRequest.builder()
                        .name(providerTransaction.getName())
                        .paymentProviderAccountRequest(PaymentProviderAccountRequest.builder()
                                .routingNumber(providerTransaction.getBankAccount().getRoutingNumber())
                                .currency(providerTransaction.getBankAccount().getCurrency())
                                .accountNumber(providerTransaction.getBankAccount().getAccountNumber()).build())
                        .build())
                .amount(providerTransaction.getAmount())
                .build();

        try {
            String requestBody = objectMapper.writeValueAsString(paymentProviderRequest);
            ResponseEntity<String> response = post(paymentProviderUrl, requestBody);
            return handleResponse(response.getBody());
        } catch (HttpClientErrorException e) {
            handlePaymentProviderApiException(e);
        } catch (Exception e) {
            log.error("Failed to create payment transaction", e);
            throw new ServiceApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return null;
    }

    private ProviderTransactionResponse handlePaymentProviderApiException(HttpClientErrorException e) {
        if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            log.error("Invalid request to Payment Provider API: " + e.getResponseBodyAsString(), e);
            throw new ServiceApiException(e.getStatusCode(), "Invalid request to provider payment API");
        } else if (e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            log.error("Internal Server error in Payment Provider API: " + e.getResponseBodyAsString(), e);
            return handleResponse(e.getResponseBodyAsString());
        } else {
            log.error("Error calling Payment Provider API: " + e.getResponseBodyAsString(), e);
            throw new ServiceApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ProviderTransactionResponse handleResponse(String responseBody) {
        try {
            PaymentProviderResponse responseEntity = objectMapper.readValue(responseBody, PaymentProviderResponse.class);
            return responseEntity.toDomain();
        } catch (JsonProcessingException e) {
            log.error("Error parsing payment provider response body: " + e.getMessage(), e);
            throw new ServiceApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
