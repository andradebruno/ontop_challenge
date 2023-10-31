package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletBalance {
    private final Long userId;
    private final BigDecimal balance;
}
