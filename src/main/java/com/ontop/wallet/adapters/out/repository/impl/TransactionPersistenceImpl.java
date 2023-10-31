package com.ontop.wallet.adapters.out.repository.impl;


import com.ontop.wallet.adapters.out.entity.TransactionEntity;
import com.ontop.wallet.adapters.out.repository.TransactionJpaRepository;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.ports.out.persistence.TransactionPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceImpl implements TransactionPersistencePort {

    private final TransactionJpaRepository transactionJpaRepository;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity transactionEntity = transactionJpaRepository.save(TransactionEntity.toEntity(transaction));

        return transactionEntity.toDomain();
    }

    @Override
    public Page<Transaction> findBySpec(Specification<TransactionEntity> specification, Pageable page) {
        if (specification != null) {
            return transactionPageToDomain(transactionJpaRepository.findAll(specification, page));
        }
        return transactionPageToDomain(transactionJpaRepository.findAll(page));
    }

    private Page<Transaction> transactionPageToDomain(Page<TransactionEntity> transactionPage) {
        return transactionPage.map(t -> t.toDomain());
    }
}
