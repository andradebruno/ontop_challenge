package com.ontop.wallet.application.ports.in;

import com.ontop.wallet.application.core.domain.Transaction;

public interface CreateTransactionUseCase {

    Transaction execute(Transaction transaction);

}
