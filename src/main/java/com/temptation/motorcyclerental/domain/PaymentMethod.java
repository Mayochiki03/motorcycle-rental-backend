package com.temptation.motorcyclerental.domain;

public enum PaymentMethod {
    CASH,
    CREDIT_CARD,
    BANK_TRANSFER,
    PROMPTPAY,
    QR_CODE;

    public static PaymentMethod fromString(String value) {
        try {
            // แปลง bank_transfer → BANK_TRANSFER
            String normalized = value.toUpperCase().replace(" ", "_");
            return PaymentMethod.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment method: " + value);
        }
    }
}