package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.Customers;
import com.temptation.motorcyclerental.obj.request.LoginRequest;
import com.temptation.motorcyclerental.obj.request.RegisterRequest;
import com.temptation.motorcyclerental.obj.response.LoginResponse;
import com.temptation.motorcyclerental.repo.CustomerRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        Optional<Customers> customerOpt = customerRepository.findByEmail(request.getEmail());

        if (customerOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), customerOpt.get().getPasswordHash())) {
            throw new RuntimeException("อีเมลหรือรหัสผ่านไม่ถูกต้อง");
        }

        Customers customer = customerOpt.get();
        String token = jwtUtil.generateToken(customer.getEmail(), "CUSTOMER");

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(customer.getCustomerId());
        response.setEmail(customer.getEmail());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setRole("CUSTOMER");
        response.setMessage("ล็อกอินสำเร็จ");

        return response;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
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

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(savedCustomer.getCustomerId());
        response.setEmail(savedCustomer.getEmail());
        response.setFirstName(savedCustomer.getFirstName());
        response.setLastName(savedCustomer.getLastName());
        response.setRole("CUSTOMER");
        response.setMessage("สมัครสมาชิกสำเร็จ");

        return response;
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