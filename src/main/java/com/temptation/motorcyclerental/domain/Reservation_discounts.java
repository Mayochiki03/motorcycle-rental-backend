package com.temptation.motorcyclerental.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_discounts")
@IdClass(ReservationDiscountId.class)
@Data
public class Reservation_discounts {

    @Id
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservations reservation;

    @Id
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    private Discounts discount;

    @Column(name = "applied_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal appliedAmount;

    @CreationTimestamp
    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;
}
