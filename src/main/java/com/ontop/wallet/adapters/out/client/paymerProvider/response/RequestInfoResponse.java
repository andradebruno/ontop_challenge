package com.ontop.wallet.adapters.out.client.paymerProvider.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ontop.wallet.application.core.domain.RequestInfo;
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
public class RequestInfoResponse {
    private String status;
    private String error;

    public RequestInfo toDomain() {
        return RequestInfo.builder()
                .status(status)
                .error(error)
                .build();
    }

}
