package com.ontop.wallet.adapters.in.resource.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.core.domain.TransactionOperation;
import com.ontop.wallet.utils.ValidCurrencyCode;
import com.ontop.wallet.utils.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
public class TransactionRequest {

    @NotNull(message = "User ID cannot be empty")
    private Long userId;

    @NotNull(message = "Bank ID cannot be empty")
    private Long bankAccountId;

    @NotNull(message = "Currency cannot be empty")
    @ValidCurrencyCode
    private String currency;

    @ValueOfEnum(enumClass = TransactionOperation.class, message = "The enum must be WITHDRAW OR TOP_UP")
    private String operation;

    @Positive(message = "Amount must be positive")
    @NotNull(message = "Amount cannot be empty")
    private BigDecimal amount;

    public Transaction toEntity() {
        return Transaction.builder().userId(userId)
                .bankAccountId(bankAccountId)
                .currency(Currency.getInstance(currency))
                .operation(TransactionOperation.valueOf(operation))
                .amount(amount).build();
    }

}
