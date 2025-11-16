package com.temptation.motorcyclerental.domain;

import com.temptation.motorcyclerental.domain.convert.MaintenanceStatusConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "motorcycles")
@Data
public class Motorcycles {

    @Id
    @Column(name = "motorcycle_id")
    private String motorcycleId;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private Integer year;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    private String color;

    @Column(name = "engine_cc")
    private Integer engineCc;

    @Column(name = "price_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = MaintenanceStatusConverter.class)
    @Column(name = "maintenance_status")
    private MaintenanceStatus maintenanceStatus = MaintenanceStatus.READY;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
