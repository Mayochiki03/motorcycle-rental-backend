package com.temptation.motorcyclerental.repocustomimpl;

import com.temptation.motorcyclerental.domain.Reservations;
import com.temptation.motorcyclerental.objc.ReservationSearchCriteria;
import com.temptation.motorcyclerental.objc.ReportSearchCriteria;
import com.temptation.motorcyclerental.repocustom.ReservationCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Reservations> searchReservations(ReservationSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservations> query = cb.createQuery(Reservations.class);
        Root<Reservations> root = query.from(Reservations.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filter by customer ID
        if (criteria.getCustomerId() != null && !criteria.getCustomerId().isEmpty()) {
            predicates.add(cb.equal(root.get("customerId"), criteria.getCustomerId()));
        }

        // Filter by motorcycle ID
        if (criteria.getMotorcycleId() != null && !criteria.getMotorcycleId().isEmpty()) {
            predicates.add(cb.equal(root.get("motorcycleId"), criteria.getMotorcycleId()));
        }

        // Filter by status
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }

        // Filter by date range
        if (criteria.getStartDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), criteria.getStartDateFrom()));
        }
        if (criteria.getStartDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), criteria.getStartDateTo()));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("createdAt")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Object[]> getReservationStats(ReportSearchCriteria criteria) {
        String jpql = "SELECT DATE(r.createdAt), COUNT(r), SUM(r.finalPrice) " +
                "FROM Reservations r " +
                "WHERE r.createdAt BETWEEN :startDate AND :endDate " +
                "GROUP BY DATE(r.createdAt) " +
                "ORDER BY DATE(r.createdAt)";

        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", criteria.getPeriodStart().atStartOfDay())
                .setParameter("endDate", criteria.getPeriodEnd().atTime(23, 59, 59))
                .getResultList();
    }

    @Override
    public List<Object[]> getRevenueReport(ReportSearchCriteria criteria) {
        String jpql = "SELECT r.status, COUNT(r), SUM(r.finalPrice) " +
                "FROM Reservations r " +
                "WHERE r.createdAt BETWEEN :startDate AND :endDate " +
                "GROUP BY r.status";

        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", criteria.getPeriodStart().atStartOfDay())
                .setParameter("endDate", criteria.getPeriodEnd().atTime(23, 59, 59))
                .getResultList();
    }

    @Override
    public Map<String, Long> getReservationStatusCounts() {
        String jpql = "SELECT r.status, COUNT(r) FROM Reservations r GROUP BY r.status";
        List<Object[]> results = entityManager.createQuery(jpql, Object[].class).getResultList();

        Map<String, Long> statusCounts = new HashMap<>();
        for (Object[] result : results) {
            statusCounts.put((String) result[0], (Long) result[1]);
        }
        return statusCounts;
    }
}