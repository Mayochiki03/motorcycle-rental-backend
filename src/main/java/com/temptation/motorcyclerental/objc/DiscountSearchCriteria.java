package com.temptation.motorcyclerental.objc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiscountSearchCriteria {
    private String discountCode;      // รหัสส่วนลด
    private String discountType;      // ประเภทส่วนลด
    private Boolean isActive;         // สถานะใช้งาน
    private LocalDate validFrom;      // วันที่เริ่มใช้งาน
    private LocalDate validTo;        // วันที่สิ้นสุด
    private String createdBy;         // สร้างโดย
}