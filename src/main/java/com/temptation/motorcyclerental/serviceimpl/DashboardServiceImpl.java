package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.obj.response.DashboardStatsResponse;
import com.temptation.motorcyclerental.repocustom.DashboardCustomRepository;
import com.temptation.motorcyclerental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardCustomRepository dashboardCustomRepository;

    @Override
    public DashboardStatsResponse getAdminDashboardStats() {
        Map<String, Object> stats = dashboardCustomRepository.getAdminDashboardStats();

        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setTotalBookings((Long) stats.get("totalReservations"));
        response.setPendingBookings((Long) stats.get("pendingReservations"));
        response.setActiveBookings((Long) stats.get("activeReservations"));
        response.setTotalRevenue((java.math.BigDecimal) stats.get("totalRevenue"));
        response.setAvailableMotorcycles((Long) stats.get("availableMotorcycles"));
        response.setTotalCustomers((Long) stats.get("totalCustomers"));
        response.setTotalEmployees(0L); // You might want to add employee count

        return response;
    }

    @Override
    public DashboardStatsResponse getEmployeeDashboardStats(String employeeId) {
        Map<String, Object> stats = dashboardCustomRepository.getEmployeeDashboardStats(employeeId);

        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setTotalBookings((Long) stats.get("managedReservations"));
        response.setPendingBookings(0L); // You might want to calculate this
        response.setActiveBookings(0L); // You might want to calculate this
        response.setTotalRevenue(java.math.BigDecimal.ZERO); // You might want to calculate this
        response.setAvailableMotorcycles(0L); // You might want to calculate this
        response.setTotalCustomers(0L);
        response.setTotalEmployees(0L);

        return response;
    }

    @Override
    public Map<String, Object> getRecentActivities(int limit) {
        return Map.of("activities", dashboardCustomRepository.getRecentActivities(limit));
    }

    @Override
    public Map<String, Object> getRevenueReport(String period) {
        // Implement revenue report logic based on period (daily, weekly, monthly)
        BigDecimal totalRevenue = dashboardCustomRepository.getTotalRevenue();
        return Map.of("totalRevenue", totalRevenue, "period", period);
    }
}