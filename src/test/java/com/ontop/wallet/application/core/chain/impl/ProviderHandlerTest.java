package com.ontop.wallet.application.core.chain.impl;

import com.ontop.wallet.application.core.chain.TransactionHandler;
import com.ontop.wallet.application.core.domain.*;
import com.ontop.wallet.application.ports.out.persistence.BankAccountPersistencePort;
import com.ontop.wallet.application.ports.out.persistence.UserPersistencePort;
import com.ontop.wallet.application.ports.out.rest.PaymentProviderRestAdapterPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProviderHandlerTest {

    @InjectMocks
    private ProviderHandler providerHandler;

    @Mock
    private PaymentProviderRestAdapterPort paymentProviderRestAdapterPort;

    @Mock
    private BankAccountPersistencePort bankAccountPersistencePort;

    @Mock
    private UserPersistencePort userPersistencePort;

    @Mock
    private TransactionHandler nextHandler;

    public ProviderHandlerTest() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(providerHandler, "onTopTransactionFee", new BigDecimal("0.1"));
        providerHandler.setNextHandler(nextHandler);
    }

    @Test
    public void testProviderHandle_WithdrawOperation() {
        User user = User.builder()
                .userId(1L)
                .firstName("Bruno").lastName("Andrade")
                .build();

        BankAccount bankAccount = BankAccount.builder()
                .bankAccountId(2L)
                .build();

        Transaction transaction = Transaction.builder()
                .operation(TransactionOperation.WITHDRAW)
                .userId(1L)
                .bankAccountId(2L)
                .amount(new BigDecimal("100.00"))
                .build();

        ProviderTransactionResponse response = ProviderTransactionResponse.builder()
                .paymentInfo(PaymentInfo.builder()
                        .providerTransactionId("12345")
                        .build())
                .requestInfo(RequestInfo.builder()
                        .status("Processing")
                        .build())
                .build();

        when(userPersistencePort.findByUserId(1L)).thenReturn(user);
        when(bankAccountPersistencePort.findByIdAndUserId(2L, 1L)).thenReturn(bankAccount);
        when(paymentProviderRestAdapterPort.createPaymentTransaction(Mockito.any(ProviderTransactionRequest.class)))
                .thenReturn(response);

        providerHandler.handle(transaction);

        assertEquals("12345", transaction.getProviderId());
        assertEquals(TransactionStatus.PROCESSING, transaction.getStatus());
        assertEquals(new BigDecimal("0.1"), transaction.getFee());

        verify(nextHandler, times(1)).handle(transaction);
    }

    @Test
    public void testProviderHandle_NonWithdrawOperation() {
        Transaction transaction = Transaction.builder()
                .operation(TransactionOperation.TOP_UP)
                .build();

        providerHandler.handle(transaction);

        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(BigDecimal.ZERO, transaction.getFee());

        verify(nextHandler, times(1)).handle(transaction);
    }
}

