package com.temptation.motorcyclerental.domain;

public enum MaintenanceStatus {
    READY,
    MAINTENANCE,
    REPAIR;

    public static MaintenanceStatus fromString(String value) {
        try {
            return MaintenanceStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid maintenance status: " + value);
        }
    }
}