package com.ontop.wallet.adapters.out.client.wallet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ontop.wallet.application.core.domain.WalletTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionResponse {

    private Long walletTransactionId;
    private BigDecimal amount;
    private Long userId;


    public WalletTransaction toDomain() {
        return WalletTransaction.builder()
                .walletTransactionId(walletTransactionId)
                .amount(amount)
                .userId(userId)
                .build();
    }
}
