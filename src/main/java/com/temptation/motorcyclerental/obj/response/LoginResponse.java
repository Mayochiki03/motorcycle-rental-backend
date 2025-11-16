package com.temptation.motorcyclerental.obj.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String message;
}