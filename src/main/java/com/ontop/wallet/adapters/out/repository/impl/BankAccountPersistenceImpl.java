package com.ontop.wallet.adapters.out.repository.impl;


import com.ontop.wallet.adapters.out.entity.BankAccountEntity;
import com.ontop.wallet.adapters.out.exception.NotFoundException;
import com.ontop.wallet.adapters.out.repository.BankAccountJpaRepository;
import com.ontop.wallet.application.core.domain.BankAccount;
import com.ontop.wallet.application.ports.out.persistence.BankAccountPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankAccountPersistenceImpl implements BankAccountPersistencePort {

    private final BankAccountJpaRepository bankAccountJpaRepository;

    @Override
    public BankAccount findByIdAndUserId(Long bankAccountId, Long userId) {
        Optional<BankAccountEntity> bankAccountEntity = bankAccountJpaRepository.findById(bankAccountId);
        if (bankAccountEntity.isEmpty()) {
            String errorMessage = String.format("Bank account not found for id=%d", bankAccountId);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        if (!bankAccountEntity.get().getUserId().equals(userId)) {
            String errorMessage = "Bank account does not belong to user";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        return bankAccountEntity.get().toDomain();
    }

    @Override
    public BankAccount save(BankAccount obj) {
        throw new UnsupportedOperationException(("Saving a BankAccount is not supported in this implementation."));
    }
}
