package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.Customers;
import com.temptation.motorcyclerental.domain.Employees;
import com.temptation.motorcyclerental.obj.request.UserRequest;
import com.temptation.motorcyclerental.obj.response.UserResponse;
import com.temptation.motorcyclerental.repo.CustomerRepository;
import com.temptation.motorcyclerental.repo.EmployeeRepository;
import com.temptation.motorcyclerental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserById(String id) {
        // Try to find in customers first
        var customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return convertCustomerToResponse(customer.get());
        }

        // Then try employees
        var employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return convertEmployeeToResponse(employee.get());
        }

        throw new RuntimeException("ไม่พบข้อมูลผู้ใช้");
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        // Try to find in customers first
        var customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return convertCustomerToResponse(customer.get());
        }

        // Then try employees
        var employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            return convertEmployeeToResponse(employee.get());
        }

        throw new RuntimeException("ไม่พบข้อมูลผู้ใช้");
    }

    @Override
    public List<UserResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertCustomerToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertEmployeeToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse createEmployee(UserRequest request, String createdBy) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("อีเมลนี้已被ใช้งานแล้ว");
        }

        Employees employee = new Employees();
        employee.setEmployeeId("EMP_" + UUID.randomUUID().toString().substring(0, 8));
        employee.setEmail(request.getEmail());
        employee.setPasswordHash(passwordEncoder.encode("temp123")); // Default password
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPhone(request.getPhone());
        employee.setPosition(request.getPosition());
        employee.setOwnerId(createdBy);

        Employees savedEmployee = employeeRepository.save(employee);
        return convertEmployeeToResponse(savedEmployee);
    }

    @Override
    public UserResponse updateUser(String id, UserRequest request) {
        // Try to update customer
        var customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customers existingCustomer = customer.get();
            existingCustomer.setFirstName(request.getFirstName());
            existingCustomer.setLastName(request.getLastName());
            existingCustomer.setPhone(request.getPhone());
            existingCustomer.setAddress(request.getAddress());
            existingCustomer.setLicenseNumber(request.getLicenseNumber());
            existingCustomer.setIdCardNumber(request.getIdCardNumber());

            Customers updatedCustomer = customerRepository.save(existingCustomer);
            return convertCustomerToResponse(updatedCustomer);
        }

        // Try to update employee
        var employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employees existingEmployee = employee.get();
            existingEmployee.setFirstName(request.getFirstName());
            existingEmployee.setLastName(request.getLastName());
            existingEmployee.setPhone(request.getPhone());
            existingEmployee.setPosition(request.getPosition());

            Employees updatedEmployee = employeeRepository.save(existingEmployee);
            return convertEmployeeToResponse(updatedEmployee);
        }

        throw new RuntimeException("ไม่พบข้อมูลผู้ใช้");
    }

    @Override
    public void deactivateUser(String id) {
        // Try to deactivate customer
        var customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customers existingCustomer = customer.get();
            // For customers, we might not want to delete, just mark as inactive
            customerRepository.save(existingCustomer);
            return;
        }

        // Try to deactivate employee
        var employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employees existingEmployee = employee.get();
            existingEmployee.setIsActive(false);
            employeeRepository.save(existingEmployee);
            return;
        }

        throw new RuntimeException("ไม่พบข้อมูลผู้ใช้");
    }

    @Override
    public UserResponse updateProfile(String userId, UserRequest request) {
        return updateUser(userId, request);
    }

    private UserResponse convertCustomerToResponse(Customers customer) {
        UserResponse response = new UserResponse();
        response.setUserId(customer.getCustomerId());
        response.setEmail(customer.getEmail());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setLicenseNumber(customer.getLicenseNumber());
        response.setRole("CUSTOMER");
        response.setIsVerified(customer.getIsVerified());
        response.setIsActive(true); // Customers are always active
        response.setCreatedAt(customer.getCreatedAt());
        return response;
    }

    private UserResponse convertEmployeeToResponse(Employees employee) {
        UserResponse response = new UserResponse();
        response.setUserId(employee.getEmployeeId());
        response.setEmail(employee.getEmail());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setPhone(employee.getPhone());
        response.setRole("EMPLOYEE");
        response.setIsVerified(true); // Employees are verified by admin
        response.setIsActive(employee.getIsActive());
        response.setCreatedAt(employee.getCreatedAt());
        return response;
    }
}