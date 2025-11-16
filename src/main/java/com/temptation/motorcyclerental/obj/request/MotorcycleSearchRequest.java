package com.temptation.motorcyclerental.obj.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MotorcycleSearchRequest {
    private String search;
    private String brand;
    private String type;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minCC;
    private Integer maxCC;
    private Boolean isAvailable;
    private String model;
}