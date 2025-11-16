package com.temptation.motorcyclerental.objc;

import lombok.Data;

@Data
public class UserSearchCriteria {
    private String search;        // ค้นหาชื่อ/อีเมล
    private String role;          // บทบาท (CUSTOMER, EMPLOYEE, ADMIN)
    private Boolean isActive;     // สถานะใช้งาน
    private Boolean isVerified;   // ยืนยันตัวตนแล้ว
    private String createdBy;     // สร้างโดย (สำหรับ employee)
}