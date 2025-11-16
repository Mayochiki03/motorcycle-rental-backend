package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Reservation_discounts;
import com.temptation.motorcyclerental.domain.ReservationDiscountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDiscountRepository extends JpaRepository<Reservation_discounts, ReservationDiscountId> {
    List<Reservation_discounts> findByReservation_ReservationId(String reservationId);

    List<Reservation_discounts> findByDiscount_DiscountId(String discountId);

    long countByDiscount_DiscountId(String discountId);
}
