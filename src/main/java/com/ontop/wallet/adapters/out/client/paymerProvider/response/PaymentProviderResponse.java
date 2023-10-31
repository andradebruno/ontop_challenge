package com.ontop.wallet.adapters.out.client.paymerProvider.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ontop.wallet.application.core.domain.ProviderTransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PaymentProviderResponse {
    @JsonProperty("requestInfo")
    private RequestInfoResponse requestInfoResponse;
    @JsonProperty("paymentInfo")
    private PaymentInfoResponse paymentInfoResponse;

    public ProviderTransactionResponse toDomain() {
        return ProviderTransactionResponse.builder()
                .requestInfo(requestInfoResponse.toDomain())
                .paymentInfo(paymentInfoResponse.toDomain())
                .build();
    }

}
