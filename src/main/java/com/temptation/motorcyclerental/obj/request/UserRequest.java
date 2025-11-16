package com.temptation.motorcyclerental.obj.request;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String licenseNumber;
    private String idCardNumber;
    private String role; // สำหรับ admin สร้างพนักงาน
    private String position; // ตำแหน่งงาน
    private Boolean isActive;
}