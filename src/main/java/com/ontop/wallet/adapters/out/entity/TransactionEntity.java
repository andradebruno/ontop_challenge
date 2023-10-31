package com.ontop.wallet.adapters.out.entity;

import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.core.domain.TransactionOperation;
import com.ontop.wallet.application.core.domain.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private Currency currency;
    private BigDecimal amount;
    private String status;
    private BigDecimal fee;
    private String operation;
    private String providerId;
    private Long bankAccountId;
    private Long walletTransactionId;
    private Long userId;
    private LocalDateTime createdAt;

    public static Specification<TransactionEntity> hasAmount(BigDecimal amount) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("amount"), amount);
    }

    public static Specification<TransactionEntity> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"),
                date.atTime(00, 00, 00),
                date.atTime(23, 59, 59));
    }

    public static Specification<TransactionEntity> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .currency(transaction.getCurrency())
                .amount(transaction.getAmount())
                .status(transaction.getStatus().getStatus())
                .fee(transaction.getFee())
                .operation(transaction.getOperation().name())
                .providerId(transaction.getProviderId())
                .walletTransactionId(transaction.getWalletTransactionId())
                .userId(transaction.getUserId())
                .createdAt(transaction.getCreatedAt()).build();
    }

    public Transaction toDomain() {
        return Transaction.builder()
                .id(id)
                .currency(currency)
                .amount(amount)
                .status(TransactionStatus.getEnumByString(status))
                .fee(fee)
                .operation(TransactionOperation.valueOf(operation))
                .providerId(providerId)
                .walletTransactionId(walletTransactionId)
                .userId(userId)
                .createdAt(createdAt).build();
    }
}
