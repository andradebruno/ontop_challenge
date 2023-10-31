package com.ontop.wallet.application.core.domain;

public enum TransactionStatus {
    CREATED("Created"),
    PROCESSING("Processing"),
    FAILED("Failed"),
    COMPLETED("Completed"),
    REFUNDED("Refunded");

    private final String status;

    TransactionStatus(String status) {
        this.status = status;
    }

    public static TransactionStatus getEnumByString(String status) {
        for (TransactionStatus e : TransactionStatus.values()) {
            if (e.status.equals(status)) return e;
        }
        return null;
    }

    public String getStatus() {
        return status;
    }
}
