package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String paymentId;
    private String reservationId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
    private String slipImageUrl;
    private String notes;
    private LocalDateTime createdAt;
}