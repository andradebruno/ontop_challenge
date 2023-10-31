package com.ontop.wallet.adapters.in.resource.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ontop.wallet.application.core.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
    private UUID id;
    private Long userId;
    private Long bankAccountId;
    private TransactionDetail transactionDetail;

    public TransactionResponse fromDomain(Transaction transaction) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .bankAccountId(transaction.getBankAccountId())
                .transactionDetail(TransactionDetail.builder()
                        .fee(transaction.getFee())
                        .status(transaction.getStatus().getStatus())
                        .amount(transaction.getAmount())
                        .operation(transaction.getOperation().name())
                        .createdAt(transaction.getCreatedAt())
                        .currency(transaction.getCurrency())
                        .build())
                .build();
    }

    public Page<TransactionResponse> transactionResponsePageFromDomain(Page<Transaction> transactionPage) {
        return transactionPage.map(transaction -> fromDomain(transaction));
    }
}
