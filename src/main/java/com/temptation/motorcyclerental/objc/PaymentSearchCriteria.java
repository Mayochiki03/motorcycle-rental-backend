package com.temptation.motorcyclerental.objc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentSearchCriteria {
    private String reservationId;     // ID การจอง
    private String paymentStatus;     // สถานะการชำระ
    private LocalDate paymentDateFrom; // วันที่ชำระ (จาก)
    private LocalDate paymentDateTo;   // วันที่ชำระ (ถึง)
    private String paymentMethod;     // วิธีการชำระ
    private String customerName;      // ชื่อลูกค้า
    private String customerEmail;     // อีเมลลูกค้า
}