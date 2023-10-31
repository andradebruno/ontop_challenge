package com.ontop.wallet.application.ports.in;

import com.ontop.wallet.application.core.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface FindTransactionsUseCase {
    Page<Transaction> execute(Long userId, Optional<BigDecimal> amount, Optional<LocalDate> date, Pageable pageable);
}
