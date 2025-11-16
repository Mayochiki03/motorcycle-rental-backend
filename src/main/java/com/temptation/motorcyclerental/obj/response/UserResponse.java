package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String licenseNumber;
    private String role;
    private Boolean isVerified;
    private Boolean isActive;
    private LocalDateTime createdAt;
}