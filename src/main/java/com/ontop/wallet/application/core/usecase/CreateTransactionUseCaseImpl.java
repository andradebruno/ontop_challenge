package com.ontop.wallet.application.core.usecase;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.chain.impl.BalanceHandler;
import com.ontop.wallet.application.core.chain.impl.BankAccountHandler;
import com.ontop.wallet.application.core.chain.impl.ProviderHandler;
import com.ontop.wallet.application.core.chain.impl.WalletHandler;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.ports.in.CreateTransactionUseCase;
import com.ontop.wallet.application.ports.out.persistence.BankAccountPersistencePort;
import com.ontop.wallet.application.ports.out.persistence.TransactionPersistencePort;
import com.ontop.wallet.application.ports.out.persistence.UserPersistencePort;
import com.ontop.wallet.application.ports.out.rest.PaymentProviderRestAdapterPort;
import com.ontop.wallet.application.ports.out.rest.WalletRestAdapterPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component //remover
public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private TransactionPersistencePort transactionPersistencePort;
    private TransactionHandler firstHandler;
    private BankAccountPersistencePort bankAccountPersistencePort;
    private WalletRestAdapterPort walletRestAdapterPort;
    private PaymentProviderRestAdapterPort paymentProviderRestAdapterPort;
    private UserPersistencePort userPersistencePort;


    public CreateTransactionUseCaseImpl(TransactionPersistencePort transactionPersistencePort,
                                        BankAccountPersistencePort bankAccountPersistencePort,
                                        WalletRestAdapterPort walletRestAdapterPort,
                                        PaymentProviderRestAdapterPort paymentProviderRestAdapterPort,
                                        UserPersistencePort userPersistencePort,
                                        @Value("${ontop.transaction.fee}") BigDecimal onTopTransactionFee) {
        this.bankAccountPersistencePort = bankAccountPersistencePort;
        this.transactionPersistencePort = transactionPersistencePort;
        this.walletRestAdapterPort = walletRestAdapterPort;
        this.userPersistencePort = userPersistencePort;
        this.paymentProviderRestAdapterPort = paymentProviderRestAdapterPort;
        TransactionHandler bankAccountHandler = new BankAccountHandler(bankAccountPersistencePort);
        TransactionHandler balanceHandler = new BalanceHandler(walletRestAdapterPort);
        TransactionHandler providerHandler = new ProviderHandler(paymentProviderRestAdapterPort,
                bankAccountPersistencePort,
                userPersistencePort, onTopTransactionFee);
        TransactionHandler walletHandler = new WalletHandler(walletRestAdapterPort);

        bankAccountHandler.setNextHandler(balanceHandler);
        balanceHandler.setNextHandler(providerHandler);
        providerHandler.setNextHandler(walletHandler);
        firstHandler = bankAccountHandler;

    }

    @Override
    public Transaction execute(Transaction transaction) {
        firstHandler.handle(transaction);
        return transactionPersistencePort.save(transaction);
    }


}
