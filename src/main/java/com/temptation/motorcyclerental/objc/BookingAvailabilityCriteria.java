package com.temptation.motorcyclerental.objc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingAvailabilityCriteria {
    private LocalDate startDate;      // วันที่เริ่มจอง
    private LocalDate endDate;        // วันที่สิ้นสุดจอง
    private String motorcycleId;      // ID รถ (ถ้าต้องการเช็คเฉพาะคัน)
    private String brand;             // ยี่ห้อ
    private String type;              // ประเภท
}