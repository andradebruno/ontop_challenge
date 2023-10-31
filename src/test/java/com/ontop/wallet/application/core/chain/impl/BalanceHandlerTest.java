package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.chain.exception.InsufficientFundsException;
import com.ontop.wallet.application.core.domain.Transaction;
import com.ontop.wallet.application.core.domain.TransactionOperation;
import com.ontop.wallet.application.core.domain.WalletBalance;
import com.ontop.wallet.application.ports.out.rest.WalletRestAdapterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BalanceHandlerTest {

    @InjectMocks
    private BalanceHandler balanceHandler;

    @Mock
    private WalletRestAdapterPort walletRestAdapterPort;

    @Mock
    private TransactionHandler nextHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        balanceHandler.setNextHandler(nextHandler);

    }

    @Test
    public void testBalanceHandle_WithSufficientBalance() {

        Transaction transaction = Transaction.builder().userId(1L).amount(new BigDecimal("50.00")).operation(TransactionOperation.WITHDRAW).build();
        WalletBalance walletBalance = WalletBalance.builder().balance(new BigDecimal("100.00")).build();

        when(walletRestAdapterPort.getUserBalance(1L)).thenReturn(walletBalance);

        assertDoesNotThrow(() -> balanceHandler.handle(transaction));

        verify(nextHandler, times(1)).handle(transaction);
    }

    @Test
    public void testBalanceHandle_WithInsufficientBalance() {
        Transaction transaction = Transaction.builder().userId(2L).amount(new BigDecimal("50.00")).operation(TransactionOperation.WITHDRAW).build();
        WalletBalance walletBalance = WalletBalance.builder().balance(new BigDecimal("30.00")).build();

        when(walletRestAdapterPort.getUserBalance(2L)).thenReturn(walletBalance);

        assertThrows(InsufficientFundsException.class, () -> balanceHandler.handle(transaction));

        verify(nextHandler, never()).handle(transaction);
    }
}
