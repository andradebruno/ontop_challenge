package com.ontop.wallet.application.core.chain;

import com.ontop.wallet.application.core.domain.Transaction;

public interface TransactionHandler {
    void handle(Transaction transaction);

    void setNextHandler(TransactionHandler nextHandler);
}
