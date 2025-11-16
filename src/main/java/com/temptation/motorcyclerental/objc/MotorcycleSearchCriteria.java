package com.temptation.motorcyclerental.objc;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MotorcycleSearchCriteria {
    private String search;        // ค้นหายี่ห้อ/รุ่น
    private String brand;         // ยี่ห้อ
    private String type;          // ประเภท (Automatic/Manual)
    private BigDecimal minPrice;  // ราคาขั้นต่ำ
    private BigDecimal maxPrice;  // ราคาสูงสุด
    private LocalDate startDate;  // วันที่เริ่มจอง
    private LocalDate endDate;    // วันที่สิ้นสุดจอง
    private Integer minCC;        // CC ขั้นต่ำ
    private Integer maxCC;        // CC สูงสุด
    private Boolean isAvailable;  // สถานะว่าง
    private String model;

}