package com.ontop.wallet.adapters.out.client.paymerProvider.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@Builder
public class PaymentProviderRequest {
    @JsonProperty("source")
    private PaymentProviderSourceRequest paymentProviderSourceRequest;
    @JsonProperty("destination")
    private PaymentProviderDestinationRequest paymentProviderDestinationRequest;
    private BigDecimal amount;
}
