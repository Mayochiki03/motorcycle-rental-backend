package com.temptation.motorcyclerental.domain;

import com.temptation.motorcyclerental.domain.convert.PaymentMethodConverter;
import com.temptation.motorcyclerental.domain.convert.PaymentStatusConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payments {
    @Id
    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "reservation_id", unique = true, nullable = false)
    private String reservationId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Convert(converter = PaymentMethodConverter.class)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Convert(converter = PaymentStatusConverter.class)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "slip_image_url")
    private String slipImageUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
