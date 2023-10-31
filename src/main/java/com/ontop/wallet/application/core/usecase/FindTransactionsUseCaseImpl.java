package com.ontop.wallet.application.core.usecase;

import com.ontop.wallet.adapters.out.entity.TransactionEntity;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.ports.in.FindTransactionsUseCase;
import com.ontop.wallet.application.ports.out.persistence.TransactionPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class FindTransactionsUseCaseImpl implements FindTransactionsUseCase {

    private TransactionPersistencePort transactionPersistencePort;

    public FindTransactionsUseCaseImpl(TransactionPersistencePort transactionPersistencePort) {
        this.transactionPersistencePort = transactionPersistencePort;
    }

    @Override
    public Page<Transaction> execute(Long userId, Optional<BigDecimal> amount, Optional<LocalDate> date, Pageable pageable) {
        Specification<TransactionEntity> specification = Specification.where(null);

        if (userId != null && userId > 0) {
            specification = TransactionEntity.hasUserId(userId);
        }

        if (amount.isPresent()) {
            if (specification != null) {
                specification = specification.and(TransactionEntity.hasAmount(amount.get()));
            } else {
                specification = TransactionEntity.hasAmount(amount.get());

            }
        }

        if (date.isPresent()) {
            if (specification != null) {
                specification = specification.and(TransactionEntity.hasDate(date.get()));
            } else {
                specification = TransactionEntity.hasDate(date.get());
            }
        }

        return transactionPersistencePort.findBySpec(specification, pageable);
    }
}
