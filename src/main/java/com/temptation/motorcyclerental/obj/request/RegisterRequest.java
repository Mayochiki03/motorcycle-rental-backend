package com.temptation.motorcyclerental.obj.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;        // ← ลบ @JsonProperty
    private String lastName;         // ← ลบ @JsonProperty
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;  // ← ลบ @JsonProperty
    private String address;
    private String licenseNumber;    // ← ลบ @JsonProperty
    private String idCardNumber;     // ← ลบ @JsonProperty
}