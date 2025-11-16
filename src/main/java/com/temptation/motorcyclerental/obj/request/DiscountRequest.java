package com.temptation.motorcyclerental.obj.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountRequest {
    private String discountCode;
    private String discountType; // PERCENTAGE, FIXED
    private BigDecimal discountValue;
    private Integer minRentalDays;
    private BigDecimal maxDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimit;
}