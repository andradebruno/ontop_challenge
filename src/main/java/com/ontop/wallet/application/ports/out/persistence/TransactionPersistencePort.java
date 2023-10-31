package com.ontop.wallet.application.ports.out.persistence;

import com.ontop.wallet.adapters.out.entity.TransactionEntity;
import com.ontop.wallet.application.core.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface TransactionPersistencePort extends DatabaseRepository<Transaction> {
    Page<Transaction> findBySpec(Specification<TransactionEntity> specification, Pageable page);
}
