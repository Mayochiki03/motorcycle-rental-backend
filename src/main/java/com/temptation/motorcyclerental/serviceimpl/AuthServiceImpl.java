package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.Customers;
import com.temptation.motorcyclerental.domain.Owners;
import com.temptation.motorcyclerental.domain.Employees;
import com.temptation.motorcyclerental.obj.request.LoginRequest;
import com.temptation.motorcyclerental.obj.request.RegisterRequest;
import com.temptation.motorcyclerental.obj.response.LoginResponse;
import com.temptation.motorcyclerental.repo.CustomerRepository;
import com.temptation.motorcyclerental.repo.OwnerRepository;
import com.temptation.motorcyclerental.repo.EmployeeRepository;
import com.temptation.motorcyclerental.service.AuthService;
import com.temptation.motorcyclerental.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        // ตรวจสอบในตาราง owners ก่อน (Admin)
        Optional<Owners> ownerOpt = ownerRepository.findByEmail(request.getEmail());
        if (ownerOpt.isPresent()) {
            Owners owner = ownerOpt.get();
            if (passwordEncoder.matches(request.getPassword(), owner.getPasswordHash())) {
                String token = jwtUtil.generateToken(owner.getEmail(), "OWNER");
                return createLoginResponse(
                        token,
                        owner.getOwnerId(),
                        owner.getEmail(),
                        owner.getFirstName(),
                        owner.getLastName(),
                        "OWNER",
                        "ล็อกอินสำเร็จ"
                );
            }
        }

        // ตรวจสอบในตาราง employees
        Optional<Employees> employeeOpt = employeeRepository.findByEmail(request.getEmail());
        if (employeeOpt.isPresent()) {
            Employees employee = employeeOpt.get();
            if (passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
                String token = jwtUtil.generateToken(employee.getEmail(), "EMPLOYEE");
                return createLoginResponse(
                        token,
                        employee.getEmployeeId(),
                        employee.getEmail(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        "EMPLOYEE",
                        "ล็อกอินสำเร็จ"
                );
            }
        }

        // ตรวจสอบในตาราง customers
        Optional<Customers> customerOpt = customerRepository.findByEmail(request.getEmail());
        if (customerOpt.isPresent()) {
            Customers customer = customerOpt.get();
            if (passwordEncoder.matches(request.getPassword(), customer.getPasswordHash())) {
                String token = jwtUtil.generateToken(customer.getEmail(), "CUSTOMER");
                return createLoginResponse(
                        token,
                        customer.getCustomerId(),
                        customer.getEmail(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        "CUSTOMER",
                        "ล็อกอินสำเร็จ"
                );
            }
        }

        throw new RuntimeException("อีเมลหรือรหัสผ่านไม่ถูกต้อง");
    }

    private LoginResponse createLoginResponse(String token, String userId, String email,
                                              String firstName, String lastName,
                                              String role, String message) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(userId);
        response.setEmail(email);
        response.setFirstName(firstName);
        response.setLastName(lastName);
        response.setRole(role);
        response.setMessage(message);
        return response;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        // ตรวจสอบว่าอีเมลซ้ำในทุกตาราง
        if (customerRepository.existsByEmail(request.getEmail()) ||
                ownerRepository.existsByEmail(request.getEmail()) ||
                employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("อีเมลนี้ใช้งานแล้ว");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("รหัสผ่านไม่ตรงกัน");
        }

        Customers customer = new Customers();
        customer.setCustomerId("CUST_" + UUID.randomUUID().toString().substring(0, 8));
        customer.setEmail(request.getEmail());
        customer.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setLicenseNumber(request.getLicenseNumber());
        customer.setIdCardNumber(request.getIdCardNumber());

        Customers savedCustomer = customerRepository.save(customer);
        String token = jwtUtil.generateToken(savedCustomer.getEmail(), "CUSTOMER");

        return createLoginResponse(
                token,
                savedCustomer.getCustomerId(),
                savedCustomer.getEmail(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                "CUSTOMER",
                "สมัครสมาชิกสำเร็จ"
        );
    }

    @Override
    public void logout(String token) {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return success
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}