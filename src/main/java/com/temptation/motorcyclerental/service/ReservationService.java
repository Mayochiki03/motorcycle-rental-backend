package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.domain.Reservations;
import com.temptation.motorcyclerental.obj.request.ReservationRequest;
import com.temptation.motorcyclerental.obj.response.ReservationResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request, String customerId);
    ReservationResponse getReservationById(String id);
    List<ReservationResponse> getReservationsByCustomer(String customerId);
    List<ReservationResponse> getAllReservations();
    ReservationResponse updateReservationStatus(String id, String status);
    void cancelReservation(String id);
    BigDecimal calculatePrice(LocalDate startDate, LocalDate endDate, BigDecimal pricePerDay, String discountCode);
}