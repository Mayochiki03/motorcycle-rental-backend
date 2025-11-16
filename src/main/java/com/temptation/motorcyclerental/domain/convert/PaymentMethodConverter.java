package com.temptation.motorcyclerental.domain.convert;

import com.temptation.motorcyclerental.domain.PaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {

    @Override
    public String convertToDatabaseColumn(PaymentMethod method) {
        return method.name().toLowerCase();
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String dbData) {
        return PaymentMethod.fromString(dbData);
    }
}