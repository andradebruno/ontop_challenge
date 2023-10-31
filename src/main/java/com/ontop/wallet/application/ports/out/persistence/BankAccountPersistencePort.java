package com.ontop.wallet.application.ports.out.persistence;

import com.ontop.wallet.application.core.domain.BankAccount;

public interface BankAccountPersistencePort extends DatabaseRepository<BankAccount> {
    BankAccount findByIdAndUserId(Long bankAccountId, Long userId);
}
