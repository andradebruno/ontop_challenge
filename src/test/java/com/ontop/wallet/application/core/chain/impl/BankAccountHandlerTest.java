package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.ports.out.persistence.BankAccountPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class BankAccountHandlerTest {

    @InjectMocks
    private BankAccountHandler bankAccountHandler;

    @Mock
    private BankAccountPersistencePort bankAccountPersistencePort;

    @Mock
    private TransactionHandler nextHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bankAccountHandler.setNextHandler(nextHandler);
    }

    @Test
    public void testBankAccountHandle_Success() {
        Transaction transaction = Transaction.builder().userId(1L).bankAccountId(2L).build();

        when(bankAccountPersistencePort.findByIdAndUserId(2L, 1L)).thenReturn(null);

        bankAccountHandler.handle(transaction);

        verify(bankAccountPersistencePort, times(1)).findByIdAndUserId(2L, 1L);

        verify(nextHandler, times(1)).handle(transaction);
    }
}
