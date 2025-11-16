package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.obj.request.PaymentRequest;
import com.temptation.motorcyclerental.obj.response.PaymentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getPaymentByReservationId(String reservationId);
    PaymentResponse updatePaymentStatus(String paymentId, String status);
    void uploadPaymentSlip(String reservationId, MultipartFile slipImage);
    List<PaymentResponse> getPaymentsByStatus(String status);
}