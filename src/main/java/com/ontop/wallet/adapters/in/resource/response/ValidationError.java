package com.ontop.wallet.adapters.in.resource.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationError {
    private String field;
    private String defaultMsg;
}
