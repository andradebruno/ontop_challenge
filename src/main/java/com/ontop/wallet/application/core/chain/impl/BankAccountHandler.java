package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.ports.out.persistence.BankAccountPersistencePort;

public class BankAccountHandler implements TransactionHandler {
    private TransactionHandler nextHandler;

    private BankAccountPersistencePort bankAccountPersistencePort;

    public BankAccountHandler(BankAccountPersistencePort bankAccountPersistencePort) {
        this.bankAccountPersistencePort = bankAccountPersistencePort;
    }


    @Override
    public void handle(Transaction transaction) {
        bankAccountPersistencePort.findByIdAndUserId(transaction.getBankAccountId(), transaction.getUserId());
        if (nextHandler != null) {
            nextHandler.handle(transaction);
        }


    }

    @Override
    public void setNextHandler(TransactionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
