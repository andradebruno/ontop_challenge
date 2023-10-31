package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.chain.exception.InsufficientFundsException;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.core.domain.TransactionOperation;
import com.ontop.wallet.application.core.domain.WalletBalance;
import com.ontop.wallet.application.ports.out.rest.WalletRestAdapterPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BalanceHandler implements TransactionHandler {
    private static final Logger log = LoggerFactory.getLogger(BalanceHandler.class);
    private TransactionHandler nextHandler;
    private WalletRestAdapterPort walletRestAdapterPort;

    public BalanceHandler(WalletRestAdapterPort walletRestAdapterPort) {
        this.walletRestAdapterPort = walletRestAdapterPort;
    }

    @Override
    public void handle(Transaction transaction) {
        if (transaction.getOperation().equals(TransactionOperation.WITHDRAW)) {
            WalletBalance walletBalance = walletRestAdapterPort.getUserBalance(transaction.getUserId());
            if (walletBalance.getBalance().compareTo(transaction.getAmount()) < 0) {
                String errorMessage = String.format("Insufficient funds for User ID=%d to complete the transaction", transaction.getUserId());
                log.error(errorMessage);
                throw new InsufficientFundsException(errorMessage);
            }
        }

        if (nextHandler != null) {
            nextHandler.handle(transaction);
        }
    }

    @Override
    public void setNextHandler(TransactionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
