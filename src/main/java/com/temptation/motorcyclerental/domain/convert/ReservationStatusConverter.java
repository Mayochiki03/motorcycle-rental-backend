package com.temptation.motorcyclerental.domain.convert;

import com.temptation.motorcyclerental.domain.ReservationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ReservationStatus status) {
        return status.name().toLowerCase();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(String dbData) {
        return ReservationStatus.fromString(dbData);
    }
}