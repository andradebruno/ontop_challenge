package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.core.domain.WalletTransaction;
import com.ontop.wallet.application.ports.out.rest.WalletRestAdapterPort;

import java.time.LocalDateTime;

public class WalletHandler implements TransactionHandler {
    private TransactionHandler nextHandler;

    private WalletRestAdapterPort walletRestAdapterPort;

    public WalletHandler(WalletRestAdapterPort walletRestAdapterPort) {
        this.walletRestAdapterPort = walletRestAdapterPort;
    }

    @Override
    public void handle(Transaction transaction) {

        WalletTransaction walletTransaction = walletRestAdapterPort.createWalletTransaction(transaction.getAmount(), transaction.getUserId());
        transaction.setWalletTransactionId(walletTransaction.getWalletTransactionId());
        transaction.setCreatedAt(LocalDateTime.now());

        if (nextHandler != null) {
            nextHandler.handle(transaction);
        }
    }

    @Override
    public void setNextHandler(TransactionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
