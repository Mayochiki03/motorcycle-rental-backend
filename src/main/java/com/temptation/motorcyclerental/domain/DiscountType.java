package com.temptation.motorcyclerental.domain;

public enum DiscountType {
    PERCENTAGE,
    FIXED;

    public static DiscountType fromString(String value) {
        try {
            return DiscountType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid discount type: " + value);
        }
    }
}