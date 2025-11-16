package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.obj.response.DashboardStatsResponse;

import java.util.Map;

public interface DashboardService {
    DashboardStatsResponse getAdminDashboardStats();
    DashboardStatsResponse getEmployeeDashboardStats(String employeeId);
    Map<String, Object> getRecentActivities(int limit);
    Map<String, Object> getRevenueReport(String period);
}