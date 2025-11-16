package com.temptation.motorcyclerental.repocustom;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DashboardCustomRepository {
    Map<String, Object> getAdminDashboardStats();
    Map<String, Object> getEmployeeDashboardStats(String employeeId);
    List<Map<String, Object>> getRecentActivities(int limit);
    List<Map<String, Object>> getTopMotorcycles(int limit);
    BigDecimal getTotalRevenue();
}