package com.ontop.wallet.adapters.out.client.paymerProvider.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ontop.wallet.application.core.domain.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class PaymentInfoResponse {
    private BigDecimal amount;
    @JsonProperty("id")
    private String providerTransactionId;

    public PaymentInfo toDomain() {
        return PaymentInfo.builder()
                .amount(amount)
                .providerTransactionId(providerTransactionId)
                .build();
    }

}
