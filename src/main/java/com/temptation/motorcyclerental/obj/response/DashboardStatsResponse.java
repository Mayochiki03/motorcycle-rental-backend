package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardStatsResponse {
    private Long totalBookings;
    private Long pendingBookings;
    private Long activeBookings;
    private BigDecimal totalRevenue;
    private Long availableMotorcycles;
    private Long totalCustomers;
    private Long totalEmployees;
}