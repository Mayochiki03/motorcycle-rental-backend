package com.temptation.motorcyclerental.repocustomimpl;

import com.temptation.motorcyclerental.domain.Motorcycles;
import com.temptation.motorcyclerental.objc.MotorcycleSearchCriteria;
import com.temptation.motorcyclerental.repocustom.MotorcycleCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MotorcycleCustomRepositoryImpl implements MotorcycleCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Motorcycles> searchMotorcycles(MotorcycleSearchCriteria criteria) {  // ← เปลี่ยนเป็น Criteria
        System.out.println("=== CUSTOM REPOSITORY: SEARCH MOTORCYCLES ===");
        System.out.println("Brand: " + criteria.getBrand());
        System.out.println("Model: " + criteria.getModel());
        System.out.println("IsAvailable: " + criteria.getIsAvailable());
        System.out.println("MinPrice: " + criteria.getMinPrice());
        System.out.println("MaxPrice: " + criteria.getMaxPrice());
        System.out.println("MinCC: " + criteria.getMinCC());
        System.out.println("MaxCC: " + criteria.getMaxCC());

        // ใช้ JPQL หรือ Criteria Query แทนการ filter ใน memory
        StringBuilder jpql = new StringBuilder("SELECT m FROM Motorcycles m WHERE 1=1");

        if (criteria.getBrand() != null && !criteria.getBrand().isEmpty()) {
            jpql.append(" AND m.brand = :brand");
        }
        if (criteria.getModel() != null && !criteria.getModel().isEmpty()) {
            jpql.append(" AND LOWER(m.model) LIKE LOWER(:model)");
        }
        if (criteria.getIsAvailable() != null) {
            jpql.append(" AND m.isAvailable = :isAvailable");
        }
        if (criteria.getMinPrice() != null) {
            jpql.append(" AND m.pricePerDay >= :minPrice");
        }
        if (criteria.getMaxPrice() != null) {
            jpql.append(" AND m.pricePerDay <= :maxPrice");
        }
        if (criteria.getMinCC() != null) {
            jpql.append(" AND m.engineCc >= :minCC");
        }
        if (criteria.getMaxCC() != null) {
            jpql.append(" AND m.engineCc <= :maxCC");
        }

        TypedQuery<Motorcycles> query = entityManager.createQuery(jpql.toString(), Motorcycles.class);

        // Set parameters
        if (criteria.getBrand() != null && !criteria.getBrand().isEmpty()) {
            query.setParameter("brand", criteria.getBrand());
        }
        if (criteria.getModel() != null && !criteria.getModel().isEmpty()) {
            query.setParameter("model", "%" + criteria.getModel() + "%");
        }
        if (criteria.getIsAvailable() != null) {
            query.setParameter("isAvailable", criteria.getIsAvailable());
        }
        if (criteria.getMinPrice() != null) {
            query.setParameter("minPrice", criteria.getMinPrice());
        }
        if (criteria.getMaxPrice() != null) {
            query.setParameter("maxPrice", criteria.getMaxPrice());
        }
        if (criteria.getMinCC() != null) {
            query.setParameter("minCC", criteria.getMinCC());
        }
        if (criteria.getMaxCC() != null) {
            query.setParameter("maxCC", criteria.getMaxCC());
        }

        List<Motorcycles> result = query.getResultList();
        System.out.println("Found " + result.size() + " motorcycles");
        return result;
    }

    @Override
    public List<Motorcycles> findAvailableMotorcycles(LocalDate startDate, LocalDate endDate) {
        System.out.println("=== FIND AVAILABLE MOTORCYCLES ===");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        String jpql = "SELECT m FROM Motorcycles m WHERE m.isAvailable = true " +
                "AND m.maintenanceStatus = 'READY' " +
                "AND m.motorcycleId NOT IN (" +
                "SELECT r.motorcycleId FROM Reservations r " +
                "WHERE r.status IN ('PENDING', 'CONFIRMED', 'APPROVED', 'ACTIVE') " +  // เพิ่ม status
                "AND (" +
                "   (r.startDate <= :endDate AND r.endDate >= :startDate) OR " +      // ซ้อนทับทั้งหมด
                "   (r.startDate BETWEEN :startDate AND :endDate) OR " +              // เริ่มต้นในช่วง
                "   (r.endDate BETWEEN :startDate AND :endDate) OR " +                // สิ้นสุดในช่วง
                "   (:startDate BETWEEN r.startDate AND r.endDate) OR " +             // วันที่เริ่มอยู่ในช่วงจอง
                "   (:endDate BETWEEN r.startDate AND r.endDate)" +                   // วันที่สิ้นสุดอยู่ในช่วงจอง
                "))";

        List<Motorcycles> result = entityManager.createQuery(jpql, Motorcycles.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        System.out.println("Available motorcycles found: " + result.size());
        result.forEach(m -> System.out.println(" - " + m.getMotorcycleId()));

        return result;
    }

    @Override
    public List<Object[]> getMotorcycleUsageStats(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT m.brand, m.model, COUNT(r.reservationId), SUM(r.finalPrice) " +
                "FROM Motorcycles m LEFT JOIN Reservations r ON m.motorcycleId = r.motorcycleId " +
                "AND r.createdAt BETWEEN :startDate AND :endDate " +
                "WHERE m.isAvailable = true " +
                "GROUP BY m.brand, m.model " +
                "ORDER BY COUNT(r.reservationId) DESC";

        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", startDate.atStartOfDay())
                .setParameter("endDate", endDate.atTime(23, 59, 59))
                .getResultList();
    }
}