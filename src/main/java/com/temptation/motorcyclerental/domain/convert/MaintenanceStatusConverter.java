package com.temptation.motorcyclerental.domain.convert;

import com.temptation.motorcyclerental.domain.MaintenanceStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MaintenanceStatusConverter implements AttributeConverter<MaintenanceStatus, String> {

    @Override
    public String convertToDatabaseColumn(MaintenanceStatus status) {
        return status.name().toLowerCase();
    }

    @Override
    public MaintenanceStatus convertToEntityAttribute(String dbData) {
        return MaintenanceStatus.fromString(dbData);
    }
}