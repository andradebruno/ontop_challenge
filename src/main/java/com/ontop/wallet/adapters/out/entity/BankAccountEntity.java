package com.ontop.wallet.adapters.out.entity;

import com.ontop.wallet.application.core.domain.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bank_account")
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountId;
    private Long userId;
    private String routingNumber;
    private String accountNumber;
    private String bankName;
    private Currency currency;
    private LocalDateTime createdAt;

    public BankAccount toDomain() {
        return BankAccount.builder()
                .bankAccountId(bankAccountId)
                .routingNumber(routingNumber)
                .bankName(bankName)
                .accountNumber(accountNumber)
                .currency(currency)
                .build();
    }

}
