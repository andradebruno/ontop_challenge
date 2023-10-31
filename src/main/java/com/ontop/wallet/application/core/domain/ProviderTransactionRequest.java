package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProviderTransactionRequest {
    private final Long userId;
    private final String name;
    private final BankAccount bankAccount;
    private final BigDecimal amount;

}
