package com.temptation.motorcyclerental.domain;

public enum PaymentStatus {
    PENDING,
    PAID,
    FAILED,
    REFUNDED;

    public static PaymentStatus fromString(String value) {
        try {
            return PaymentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + value);
        }
    }
}