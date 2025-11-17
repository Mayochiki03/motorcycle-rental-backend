package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.ReservationStatus;
import com.temptation.motorcyclerental.domain.Reservations;
import com.temptation.motorcyclerental.obj.request.ReservationRequest;
import com.temptation.motorcyclerental.obj.response.ReservationResponse;
import com.temptation.motorcyclerental.repo.CustomerRepository;
import com.temptation.motorcyclerental.repo.MotorcycleRepository;
import com.temptation.motorcyclerental.repo.ReservationRepository;
import com.temptation.motorcyclerental.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ReservationResponse createReservation(ReservationRequest request, String customerId) {
        // Validate customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("ไม่พบข้อมูลลูกค้า");
        }

        // Validate motorcycle exists and is available
        var motorcycle = motorcycleRepository.findById(request.getMotorcycleId())
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลรถ"));

        if (!motorcycle.getIsAvailable()) {
            throw new RuntimeException("รถไม่พร้อมให้บริการ");
        }

        // ✅ ตรวจสอบการจองซ้อน
        List<Reservations> existingReservations = reservationRepository.findByMotorcycleId(request.getMotorcycleId());

        for (Reservations existing : existingReservations) {
            // ข้ามการจองที่ยกเลิกแล้ว
            if (existing.getStatus() == ReservationStatus.CANCELLED) {
                continue;
            }

            // ตรวจสอบวันที่ซ้อนทับ
            boolean isOverlap = !(request.getEndDate().isBefore(existing.getStartDate()) ||
                    request.getStartDate().isAfter(existing.getEndDate()));

            if (isOverlap) {
                throw new RuntimeException("รถไม่ว่างในช่วงวันที่ " +
                        existing.getStartDate() + " ถึง " + existing.getEndDate() +
                        " กรุณาเลือกวันที่อื่น");
            }
        }

        // Calculate price
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) {
            throw new RuntimeException("วันที่ไม่ถูกต้อง");
        }

        BigDecimal totalPrice = motorcycle.getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalPrice = totalPrice.subtract(discountAmount);

        // Create reservation
        Reservations reservation = new Reservations();
        reservation.setReservationId("RES_" + UUID.randomUUID().toString().substring(0, 8));
        reservation.setCustomerId(customerId);
        reservation.setMotorcycleId(request.getMotorcycleId());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setTotalDays((int) days);
        reservation.setTotalPrice(totalPrice);
        reservation.setDiscountAmount(discountAmount);
        reservation.setFinalPrice(finalPrice);
        reservation.setDepositAmount(finalPrice.multiply(BigDecimal.valueOf(0.3))); // 30% deposit
        reservation.setReturnLocation(request.getReturnLocation());
        reservation.setSpecialRequests(request.getSpecialRequests());

        Reservations savedReservation = reservationRepository.save(reservation);
        return convertToResponse(savedReservation);
    }

    @Override
    public ReservationResponse getReservationById(String id) {
        Reservations reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการจอง"));
        return convertToResponse(reservation);
    }

    @Override
    public List<ReservationResponse> getReservationsByCustomer(String customerId) {
        return reservationRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse updateReservationStatus(String id, String status) {
        Reservations reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการจอง"));

        reservation.setStatus(ReservationStatus.valueOf(status.toUpperCase()));
        Reservations updatedReservation = reservationRepository.save(reservation);
        return convertToResponse(updatedReservation);
    }

    @Override
    public void cancelReservation(String id) {
        Reservations reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการจอง"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Override
    public BigDecimal calculatePrice(LocalDate startDate, LocalDate endDate, BigDecimal pricePerDay, String discountCode) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));

        // Apply discount logic here (50 THB every 3 days)
        BigDecimal discount = BigDecimal.ZERO;
        if (days >= 3) {
            discount = BigDecimal.valueOf((days / 3) * 50);
        }

        return totalPrice.subtract(discount);
    }

    private ReservationResponse convertToResponse(Reservations reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setReservationId(reservation.getReservationId());
        response.setCustomerId(reservation.getCustomerId());
        response.setMotorcycleId(reservation.getMotorcycleId());
        response.setStartDate(reservation.getStartDate());
        response.setEndDate(reservation.getEndDate());
        response.setTotalDays(reservation.getTotalDays());
        response.setTotalPrice(reservation.getTotalPrice());
        response.setDiscountAmount(reservation.getDiscountAmount());
        response.setFinalPrice(reservation.getFinalPrice());
        response.setStatus(reservation.getStatus().name());
        response.setPickupLocation(reservation.getPickupLocation());
        response.setReturnLocation(reservation.getReturnLocation());
        response.setCreatedAt(reservation.getCreatedAt());
        return response;
    }
}