package com.ontop.wallet.adapters.in.resource.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String errorCode;
    private List<ValidationError> validationError;
    private String errorMsg;
    private Integer statusCode;
    private LocalDateTime timestamp;
}
