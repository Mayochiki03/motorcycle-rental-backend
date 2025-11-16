package com.temptation.motorcyclerental.objc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportSearchCriteria {
    private LocalDate periodStart;    // วันที่เริ่มต้นช่วงรายงาน
    private LocalDate periodEnd;      // วันที่สิ้นสุดช่วงรายงาน
    private String reportType;        // ประเภทรายงาน (daily, monthly, yearly)
    private String groupBy;           // จัดกลุ่มตาม (day, month, motorcycle, customer)
    private String motorcycleBrand;   // กรองตามยี่ห้อรถ
    private String customerType;      // กรองตามประเภทลูกค้า
}