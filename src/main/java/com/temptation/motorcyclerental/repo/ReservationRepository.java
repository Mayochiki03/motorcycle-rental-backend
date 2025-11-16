package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservations, String> {
    List<Reservations> findByCustomerId(String customerId);
    List<Reservations> findByMotorcycleId(String motorcycleId);
    List<Reservations> findByStatus(String status);
    List<Reservations> findByEmployeeId(String employeeId);

    @Query("SELECT r FROM Reservations r WHERE r.startDate BETWEEN :startDate AND :endDate")
    List<Reservations> findReservationsBetweenDates(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(r) FROM Reservations r WHERE r.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT r FROM Reservations r WHERE r.customerId = :customerId ORDER BY r.createdAt DESC")
    List<Reservations> findRecentReservationsByCustomer(@Param("customerId") String customerId);
}