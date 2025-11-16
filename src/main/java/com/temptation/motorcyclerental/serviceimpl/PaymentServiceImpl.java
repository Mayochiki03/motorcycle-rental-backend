package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.Payments;
import com.temptation.motorcyclerental.obj.request.PaymentRequest;
import com.temptation.motorcyclerental.obj.response.PaymentResponse;
import com.temptation.motorcyclerental.repo.PaymentRepository;
import com.temptation.motorcyclerental.repo.ReservationRepository;
import com.temptation.motorcyclerental.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Check if reservation exists
        var reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการจอง"));

        // Create payment record
        Payments payment = new Payments();
        payment.setPaymentId("PAY_" + UUID.randomUUID().toString().substring(0, 8));
        payment.setReservationId(request.getReservationId());
        payment.setAmount(reservation.getFinalPrice());
        payment.setPaymentMethod(com.temptation.motorcyclerental.domain.PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setPaymentStatus(com.temptation.motorcyclerental.domain.PaymentStatus.PENDING);
        payment.setNotes(request.getNotes());

        Payments savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentByReservationId(String reservationId) {
        Payments payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการชำระเงิน"));
        return convertToResponse(payment);
    }

    @Override
    public PaymentResponse updatePaymentStatus(String paymentId, String status) {
        Payments payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการชำระเงิน"));

        payment.setPaymentStatus(com.temptation.motorcyclerental.domain.PaymentStatus.valueOf(status));
        if (status.equals("PAID")) {
            payment.setPaymentDate(java.time.LocalDateTime.now());
        }

        Payments updatedPayment = paymentRepository.save(payment);
        return convertToResponse(updatedPayment);
    }

    @Override
    public void uploadPaymentSlip(String reservationId, MultipartFile slipImage) {
        Payments payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการชำระเงิน"));

        // In a real application, you would save the file and store the path
        String slipImageUrl = "/uploads/slips/" + slipImage.getOriginalFilename();
        payment.setSlipImageUrl(slipImageUrl);

        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse convertToResponse(Payments payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setReservationId(payment.getReservationId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod().name());
        response.setPaymentStatus(payment.getPaymentStatus().name());
        response.setPaymentDate(payment.getPaymentDate());
        response.setTransactionId(payment.getTransactionId());
        response.setSlipImageUrl(payment.getSlipImageUrl());
        response.setNotes(payment.getNotes());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}