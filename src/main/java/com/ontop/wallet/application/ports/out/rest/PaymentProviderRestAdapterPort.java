package com.ontop.wallet.application.ports.out.rest;

import com.ontop.wallet.application.core.domain.ProviderTransactionRequest;
import com.ontop.wallet.application.core.domain.ProviderTransactionResponse;

public interface PaymentProviderRestAdapterPort {
    ProviderTransactionResponse createPaymentTransaction(ProviderTransactionRequest providerTransaction);
}
