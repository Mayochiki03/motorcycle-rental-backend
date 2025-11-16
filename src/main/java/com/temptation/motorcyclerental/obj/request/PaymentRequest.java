package com.temptation.motorcyclerental.obj.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PaymentRequest {
    private String reservationId;
    private String paymentMethod; // CASH, CREDIT_CARD, BANK_TRANSFER, QR_CODE
    private MultipartFile slipImage; // ไฟล์สลิปการโอน
    private String notes;
}