package com.temptation.motorcyclerental.domain.convert;

import com.temptation.motorcyclerental.domain.DiscountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DiscountTypeConverter implements AttributeConverter<DiscountType, String> {

    @Override
    public String convertToDatabaseColumn(DiscountType type) {
        return type.name().toLowerCase();
    }

    @Override
    public DiscountType convertToEntityAttribute(String dbData) {
        return DiscountType.fromString(dbData);
    }
}