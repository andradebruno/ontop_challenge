package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.core.domain.WalletTransaction;
import com.ontop.wallet.application.ports.out.rest.WalletRestAdapterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class WalletHandlerTest {

    @Mock
    private WalletRestAdapterPort walletRestAdapterPort;
    @InjectMocks
    private WalletHandler walletHandler;
    @Mock
    private TransactionHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testWalletHandle_Success() {
        Transaction transaction = Transaction.builder().amount(new BigDecimal("100.00")).userId(1L).build();


        WalletTransaction walletTransaction = WalletTransaction.builder().walletTransactionId(12345L).build();

        when(walletRestAdapterPort.createWalletTransaction(transaction.getAmount(), transaction.getUserId()))
                .thenReturn(walletTransaction);

        walletHandler.handle(transaction);

        assertEquals(walletTransaction.getWalletTransactionId(), transaction.getWalletTransactionId());
        assertNotNull(transaction.getCreatedAt());

        verify(handler, never()).handle(transaction);

    }
}
