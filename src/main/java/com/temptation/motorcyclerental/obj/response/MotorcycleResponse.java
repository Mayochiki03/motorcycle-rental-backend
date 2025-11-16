package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MotorcycleResponse {
    private String motorcycleId;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;
    private Integer engineCc;
    private BigDecimal pricePerDay;
    private String imageUrl;
    private Boolean isAvailable;
    private String description;
    private String maintenanceStatus;
    private LocalDateTime createdAt;
}