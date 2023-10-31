package com.ontop.wallet.application.core.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Currency;

@Data
@Builder
public class BankAccount {
    private final Long bankAccountId;
    private final String bankName;
    private final String accountNumber;
    private final Currency currency;
    private final String routingNumber;

}
