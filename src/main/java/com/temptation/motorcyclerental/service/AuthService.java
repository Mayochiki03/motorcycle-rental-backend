package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.obj.request.LoginRequest;
import com.temptation.motorcyclerental.obj.request.RegisterRequest;
import com.temptation.motorcyclerental.obj.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    void logout(String token);
    boolean validateToken(String token);
}