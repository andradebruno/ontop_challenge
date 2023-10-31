package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String idType;
    private final String nationalIdNumber;

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
