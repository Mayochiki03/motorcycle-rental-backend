package com.temptation.motorcyclerental.objc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationSearchCriteria {
    private String customerId;        // ID ลูกค้า
    private String motorcycleId;      // ID รถ
    private String status;            // สถานะการจอง
    private LocalDate startDateFrom;  // วันที่เริ่มจอง (จาก)
    private LocalDate startDateTo;    // วันที่เริ่มจอง (ถึง)
    private String customerName;      // ชื่อลูกค้า (สำหรับค้นหา)
    private String motorcycleModel;   // รุ่นรถ (สำหรับค้นหา)
    private String customerEmail;     // อีเมลลูกค้า
}