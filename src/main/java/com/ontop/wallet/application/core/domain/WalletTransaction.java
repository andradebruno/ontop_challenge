package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletTransaction {
    private final Long userId;
    private final BigDecimal amount;
    private final Long walletTransactionId;
}
