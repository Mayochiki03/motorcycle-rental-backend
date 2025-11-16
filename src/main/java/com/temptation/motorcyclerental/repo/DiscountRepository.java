package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discounts, String> {
    Optional<Discounts> findByDiscountCode(String discountCode);
    List<Discounts> findByIsActiveTrue();
    List<Discounts> findByCreatedBy(String createdBy);

    @Query("SELECT d FROM Discounts d WHERE d.isActive = true AND d.startDate <= :currentDate AND d.endDate >= :currentDate")
    List<Discounts> findActiveDiscounts(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT d FROM Discounts d WHERE d.discountCode = :discountCode AND d.isActive = true AND d.startDate <= :currentDate AND d.endDate >= :currentDate")
    Optional<Discounts> findValidDiscountByCode(@Param("discountCode") String discountCode,
                                               @Param("currentDate") LocalDate currentDate);

    boolean existsByDiscountCode(String discountCode);
}