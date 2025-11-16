package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DiscountResponse {
    private String discountId;
    private String discountCode;
    private String discountType; // PERCENTAGE, FIXED
    private BigDecimal discountValue;
    private Integer minRentalDays;
    private BigDecimal maxDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private String createdBy;
    private Integer usageLimit;
    private Integer usedCount;
    private LocalDateTime createdAt;
}