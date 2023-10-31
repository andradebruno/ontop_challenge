package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderTransactionResponse {
    private final RequestInfo requestInfo;
    private final PaymentInfo paymentInfo;

}
