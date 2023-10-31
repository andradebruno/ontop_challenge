package com.ontop.wallet.application.ports.out.rest;

import com.ontop.wallet.application.core.domain.WalletBalance;
import com.ontop.wallet.application.core.domain.WalletTransaction;

import java.math.BigDecimal;

public interface WalletRestAdapterPort {
    WalletBalance getUserBalance(Long userId);

    WalletTransaction createWalletTransaction(BigDecimal amount, Long userId);
}
