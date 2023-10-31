package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Data
@Builder
public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private Long userId;
    private TransactionStatus status;
    private BigDecimal fee;
    private TransactionOperation operation;
    private String providerId;
    private Long bankAccountId;
    private Long walletTransactionId;
    private Currency currency;
    private LocalDateTime createdAt;
}
