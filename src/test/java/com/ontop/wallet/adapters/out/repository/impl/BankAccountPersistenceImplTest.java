package com.ontop.wallet.adapters.out.repository.impl;

import com.ontop.wallet.adapters.out.entity.BankAccountEntity;
import com.ontop.wallet.adapters.out.exception.NotFoundException;
import com.ontop.wallet.adapters.out.repository.BankAccountJpaRepository;
import com.ontop.wallet.application.core.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BankAccountPersistenceImplTest {

    @InjectMocks
    private BankAccountPersistenceImpl bankAccountPersistence;
    @Mock
    private BankAccountJpaRepository bankAccountJpaRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindBankAccountByIdAndUserId_AccountFound() {
        BankAccountEntity bankAccount = BankAccountEntity.builder().bankAccountId(1L)
                .userId(123L)
                .accountNumber("123")
                .routingNumber("321")
                .bankName("Test")
                .currency(Currency.getInstance("USD"))
                .build();

        when(bankAccountJpaRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        BankAccount response = bankAccountPersistence.findByIdAndUserId(1L, 123L);

        verify(bankAccountJpaRepository, times(1)).findById(1L);
        assertEquals(bankAccount.toDomain(), response);
    }

    @Test
    public void testFindBankAccountByIdAndUserId_AccountNotFound() {
        when(bankAccountJpaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bankAccountPersistence.findByIdAndUserId(1L, 123L));

        verify(bankAccountJpaRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindBankAccountByIdAndUserId_AccountNotBelongToUser() {
        BankAccountEntity bankAccount = BankAccountEntity.builder().bankAccountId(1L)
                .userId(456L)
                .accountNumber("123")
                .routingNumber("321")
                .bankName("Test")
                .currency(Currency.getInstance("USD"))
                .build();
        when(bankAccountJpaRepository.findById(1L)).thenReturn(Optional.of(bankAccount));
        assertThrows(NotFoundException.class, () -> bankAccountPersistence.findByIdAndUserId(1L, 123L));
        verify(bankAccountJpaRepository, times(1)).findById(1L);
    }
}
