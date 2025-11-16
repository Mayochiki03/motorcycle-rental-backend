package com.temptation.motorcyclerental.domain;

import com.temptation.motorcyclerental.domain.convert.ReservationStatusConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
public class Reservations {

    @Id
    @Column(name = "reservation_id")
    private String reservationId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "motorcycle_id", nullable = false)
    private String motorcycleId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Convert(converter = ReservationStatusConverter.class)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "deposit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "final_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "return_location")
    private String returnLocation;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
