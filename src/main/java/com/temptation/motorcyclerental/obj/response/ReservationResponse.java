package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private String reservationId;
    private String customerId;
    private String customerName;
    private String motorcycleId;
    private String motorcycleInfo; // Brand + Model
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private BigDecimal totalPrice;
    private BigDecimal discountAmount;
    private BigDecimal finalPrice;
    private String status;
    private String pickupLocation;
    private String returnLocation;
    private LocalDateTime createdAt;
}