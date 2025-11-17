package com.temptation.motorcyclerental.repocustomimpl;

import com.temptation.motorcyclerental.repocustom.DashboardCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardCustomRepositoryImpl implements DashboardCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total reservations
        String totalReservationsQuery = "SELECT COUNT(r) FROM Reservations r";
        Long totalReservations = entityManager.createQuery(totalReservationsQuery, Long.class).getSingleResult();
        stats.put("totalReservations", totalReservations);

        // Pending reservations
        String pendingReservationsQuery = "SELECT COUNT(r) FROM Reservations r WHERE r.status = 'PENDING'";
        Long pendingReservations = entityManager.createQuery(pendingReservationsQuery, Long.class).getSingleResult();
        stats.put("pendingReservations", pendingReservations);

        // Active reservations
        String activeReservationsQuery = "SELECT COUNT(r) FROM Reservations r WHERE r.status = 'ACTIVE'";
        Long activeReservations = entityManager.createQuery(activeReservationsQuery, Long.class).getSingleResult();
        stats.put("activeReservations", activeReservations);

        // Total revenue
        String revenueQuery = "SELECT COALESCE(SUM(r.finalPrice), 0) FROM Reservations r WHERE r.status IN ('CONFIRMED', 'ACTIVE', 'COMPLETED')";
        BigDecimal totalRevenue = entityManager.createQuery(revenueQuery, BigDecimal.class).getSingleResult();
        stats.put("totalRevenue", totalRevenue);

        // Available motorcycles
        String availableMotorcyclesQuery = "SELECT COUNT(m) FROM Motorcycles m WHERE m.isAvailable = true";
        Long availableMotorcycles = entityManager.createQuery(availableMotorcyclesQuery, Long.class).getSingleResult();
        stats.put("availableMotorcycles", availableMotorcycles);

        // Total customers
        String totalCustomersQuery = "SELECT COUNT(c) FROM Customers c";
        Long totalCustomers = entityManager.createQuery(totalCustomersQuery, Long.class).getSingleResult();
        stats.put("totalCustomers", totalCustomers);

        return stats;
    }

    @Override
    public Map<String, Object> getEmployeeDashboardStats(String employeeId) {
        Map<String, Object> stats = new HashMap<>();

        // Employee's managed reservations
        String managedReservationsQuery = "SELECT COUNT(r) FROM Reservations r WHERE r.employeeId = :employeeId";
        Long managedReservations = entityManager.createQuery(managedReservationsQuery, Long.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult();
        stats.put("managedReservations", managedReservations);

        // Today's reservations
        String todayReservationsQuery = "SELECT COUNT(r) FROM Reservations r WHERE r.employeeId = :employeeId AND DATE(r.createdAt) = CURRENT_DATE";
        Long todayReservations = entityManager.createQuery(todayReservationsQuery, Long.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult();
        stats.put("todayReservations", todayReservations);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getRecentActivities(int limit) {
        String jpql = "SELECT r.reservationId, c.firstName, c.lastName, m.brand, m.model, r.status, r.createdAt " +
                "FROM Reservations r " +
                "JOIN Customers c ON r.customerId = c.customerId " +
                "JOIN Motorcycles m ON r.motorcycleId = m.motorcycleId " +
                "ORDER BY r.createdAt DESC";

        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setMaxResults(limit)
                .getResultList();

        return results.stream().map(result -> {
            Map<String, Object> activity = new HashMap<>();
            activity.put("reservationId", result[0]);
            activity.put("customerName", result[1] + " " + result[2]);
            activity.put("motorcycleInfo", result[3] + " " + result[4]);
            activity.put("status", result[5]);
            activity.put("createdAt", result[6]);
            return activity;
        }).toList();
    }

    @Override
    public List<Map<String, Object>> getTopMotorcycles(int limit) {
        String jpql = "SELECT m.brand, m.model, COUNT(r.reservationId) as rentalCount " +
                "FROM Motorcycles m LEFT JOIN Reservations r ON m.motorcycleId = r.motorcycleId " +
                "GROUP BY m.brand, m.model " +
                "ORDER BY rentalCount DESC";

        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setMaxResults(limit)
                .getResultList();

        return results.stream().map(result -> {
            Map<String, Object> motorcycle = new HashMap<>();
            motorcycle.put("brand", result[0]);
            motorcycle.put("model", result[1]);
            motorcycle.put("rentalCount", result[2]);
            return motorcycle;
        }).toList();
    }

    @Override
    public BigDecimal getTotalRevenue() {
        String jpql = "SELECT COALESCE(SUM(r.finalPrice), 0) FROM Reservations r WHERE r.status IN ('CONFIRMED', 'ACTIVE', 'COMPLETED')";
        return entityManager.createQuery(jpql, BigDecimal.class).getSingleResult();
    }
}