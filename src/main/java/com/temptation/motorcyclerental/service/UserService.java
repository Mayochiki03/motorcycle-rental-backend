package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.obj.request.UserRequest;
import com.temptation.motorcyclerental.obj.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserById(String id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllCustomers();
    List<UserResponse> getAllEmployees();
    UserResponse createEmployee(UserRequest request, String createdBy);
    UserResponse updateUser(String id, UserRequest request);
    void deactivateUser(String id);
    UserResponse updateProfile(String userId, UserRequest request);
}