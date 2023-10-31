package com.ontop.wallet.adapters.out.client.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.wallet.adapters.in.resource.exception.ServiceApiException;
import com.ontop.wallet.adapters.out.client.RestClient;
import com.ontop.wallet.adapters.out.client.wallet.request.WalletTransactionRequest;
import com.ontop.wallet.adapters.out.client.wallet.response.WalletBalanceResponse;
import com.ontop.wallet.adapters.out.client.wallet.response.WalletTransactionResponse;
import com.ontop.wallet.application.core.domain.WalletBalance;
import com.ontop.wallet.application.core.domain.WalletTransaction;
import com.ontop.wallet.application.ports.out.rest.WalletRestAdapterPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Component
public class WalletRestAdapter extends RestClient implements WalletRestAdapterPort {
    private final String walletBalanceUrl;
    private final String walletTransactionUrl;


    public WalletRestAdapter(RestTemplate walletRestTemplate,
                             @Value("${url.wallet.balance}") String walletBalanceUrl,
                             @Value("${url.wallet.transaction}") String walletTransactionUrl) {
        super(walletRestTemplate);
        objectMapper = new ObjectMapper();
        this.walletBalanceUrl = walletBalanceUrl;
        this.walletTransactionUrl = walletTransactionUrl;
    }


    @Override
    public WalletBalance getUserBalance(Long userId) {
        try {
            ResponseEntity<String> response = this.get(String.format(walletBalanceUrl, userId));
            String responseBody = response.getBody();
            WalletBalanceResponse walletBalanceResponse = objectMapper.readValue(responseBody,
                    WalletBalanceResponse.class);
            return walletBalanceResponse.toDomain();

        } catch (HttpClientErrorException e) {
            handleWalletApiException(e, userId);
        } catch (Exception e) {
            log.error(String.format("Error getting balance for user %d", userId), e);
            throw new ServiceApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting user balance");
        }
        return WalletBalance.builder().build();
    }

    @Override
    public WalletTransaction createWalletTransaction(BigDecimal amount, Long userId) {
        try {
            String requestBody = objectMapper.writeValueAsString(
                    WalletTransactionRequest.builder().amount(amount).userId(userId).build());

            ResponseEntity<String> response = this.post(walletTransactionUrl, requestBody);

            String responseBody = response.getBody();

            WalletTransactionResponse transactionResponse = objectMapper.readValue(responseBody,
                    WalletTransactionResponse.class);

            return transactionResponse.toDomain();

        } catch (HttpClientErrorException e) {
            handleWalletApiException(e, userId);
        } catch (Exception e) {
            log.error("Error creating wallet transaction", e);
            throw new ServiceApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating wallet transaction");
        }
        return WalletTransaction.builder().build();
    }

    private void handleWalletApiException(HttpClientErrorException e, Long userId) {
        if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            log.error("Invalid request to Wallet API", e);
            throw new ServiceApiException(e.getStatusCode(), "Invalid request to Wallet API");
        } else if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            log.error(String.format("User ID=%d not found", userId), e);
            throw new ServiceApiException(e.getStatusCode(), String.format("User ID=%d not found", userId));
        } else {
            log.error("Error in the Wallet API", e);
            throw new ServiceApiException(e.getStatusCode(), e.getMessage());
        }
    }
}
