package com.temptation.motorcyclerental.domain;

public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    ACTIVE,
    COMPLETED,
    CANCELLED;

    public static ReservationStatus fromString(String value) {
        try {
            return ReservationStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid reservation status: " + value);
        }
    }
}