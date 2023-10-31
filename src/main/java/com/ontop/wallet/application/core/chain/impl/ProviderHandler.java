package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.domain.*;
import com.ontop.wallet.application.ports.out.persistence.BankAccountPersistencePort;
import com.ontop.wallet.application.ports.out.persistence.UserPersistencePort;
import com.ontop.wallet.application.ports.out.rest.PaymentProviderRestAdapterPort;

import java.math.BigDecimal;

public class ProviderHandler implements TransactionHandler {
    private TransactionHandler nextHandler;
    private PaymentProviderRestAdapterPort paymentProviderRestAdapterPort;
    private BankAccountPersistencePort bankAccountPersistencePort;
    private UserPersistencePort userPersistencePort;
    private BigDecimal onTopTransactionFee;

    public ProviderHandler(PaymentProviderRestAdapterPort paymentProviderRestAdapterPort,
                           BankAccountPersistencePort bankAccountPersistencePort,
                           UserPersistencePort userPersistencePort,
                           BigDecimal onTopTransactionFee) {
        this.paymentProviderRestAdapterPort = paymentProviderRestAdapterPort;
        this.bankAccountPersistencePort = bankAccountPersistencePort;
        this.userPersistencePort = userPersistencePort;
        this.onTopTransactionFee = onTopTransactionFee;
    }

    @Override
    public void handle(Transaction transaction) {
        if (transaction.getOperation().equals(TransactionOperation.WITHDRAW)) {
            User user = userPersistencePort.findByUserId(transaction.getUserId());
            BankAccount bankAccount = bankAccountPersistencePort.findByIdAndUserId(transaction.getBankAccountId(),
                    transaction.getUserId());
            BigDecimal transactionAmountAfterFee = transaction.getAmount().subtract(
                    transaction.getAmount().multiply(onTopTransactionFee));


            ProviderTransactionResponse providerTransactionResponse = paymentProviderRestAdapterPort
                    .createPaymentTransaction(ProviderTransactionRequest.builder()
                            .userId(transaction.getUserId())
                            .amount(transactionAmountAfterFee)
                            .bankAccount(bankAccount)
                            .name(user.getFullName()).build());
            transaction.setProviderId(providerTransactionResponse.getPaymentInfo().getProviderTransactionId());
            transaction.setFee(onTopTransactionFee);
            transaction.setStatus(TransactionStatus.getEnumByString(providerTransactionResponse.getRequestInfo().getStatus()));
        } else {
            transaction.setFee(BigDecimal.ZERO);
            transaction.setStatus(TransactionStatus.COMPLETED);
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
