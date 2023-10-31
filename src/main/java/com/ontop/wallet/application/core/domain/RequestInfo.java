package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestInfo {
    private final String status;
    private final String error;
}
