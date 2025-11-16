package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, String> {
    Optional<Payments> findByReservationId(String reservationId);
    List<Payments> findByPaymentStatus(String paymentStatus);
    List<Payments> findByPaymentMethod(String paymentMethod);

    @Query("SELECT p FROM Payments p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payments> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.amount) FROM Payments p WHERE p.paymentStatus = 'PAID' AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
}