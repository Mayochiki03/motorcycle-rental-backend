package com.temptation.motorcyclerental.controller;

import com.temptation.motorcyclerental.domain.Motorcycles;
import com.temptation.motorcyclerental.obj.request.*;
import com.temptation.motorcyclerental.obj.response.*;
import com.temptation.motorcyclerental.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MotorcycleRentalController {

    // Services
    private final AuthService authService;
    private final MotorcycleService motorcycleService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final DiscountService discountService;
    private final DashboardService dashboardService;

    // ==================== AUTHENTICATION ====================
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("ล็อกอินสำเร็จ", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@RequestBody RegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success("สมัครสมาชิกสำเร็จ", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        try {
            authService.logout(token.replace("Bearer ", ""));
            return ResponseEntity.ok(ApiResponse.success("ล็อกเอ้าท์สำเร็จ", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== MOTORCYCLES ====================
    @GetMapping("/motorcycles")
    public ResponseEntity<ApiResponse<List<MotorcycleResponse>>> getAllMotorcycles() {
        try {
            List<MotorcycleResponse> motorcycles = motorcycleService.getAllMotorcycles();
            return ResponseEntity.ok(ApiResponse.success(motorcycles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/motorcycles/{id}")
    public ResponseEntity<ApiResponse<MotorcycleResponse>> getMotorcycleById(@PathVariable String id) {
        try {
            MotorcycleResponse motorcycle = motorcycleService.getMotorcycleById(id);
            return ResponseEntity.ok(ApiResponse.success(motorcycle));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/motorcycles/search")
    public ResponseEntity<ApiResponse<List<MotorcycleResponse>>> searchMotorcycles(@RequestBody MotorcycleSearchRequest request) {
        System.out.println("=== CONTROLLER: /motorcycles/search CALLED ===");
        System.out.println("Request Brand: " + request.getBrand());
        System.out.println("Request IsAvailable: " + request.getIsAvailable());

        try {
            List<MotorcycleResponse> motorcycles = motorcycleService.searchMotorcycles(request);
            System.out.println("Service returned: " + motorcycles.size() + " motorcycles");
            return ResponseEntity.ok(ApiResponse.success(motorcycles));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/motorcycles/available")
    public ResponseEntity<ApiResponse<List<MotorcycleResponse>>> getAvailableMotorcycles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<MotorcycleResponse> motorcycles = motorcycleService.getAvailableMotorcycles(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(motorcycles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/admin/motorcycles")
    public ResponseEntity<ApiResponse<MotorcycleResponse>> createMotorcycle(@RequestBody Motorcycles motorcycle) {
        try {
            MotorcycleResponse createdMotorcycle = motorcycleService.createMotorcycle(motorcycle);
            return ResponseEntity.ok(ApiResponse.success("สร้างข้อมูลรถสำเร็จ", createdMotorcycle));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/admin/motorcycles/{id}")
    public ResponseEntity<ApiResponse<MotorcycleResponse>> updateMotorcycle(@PathVariable String id, @RequestBody Motorcycles motorcycle) {
        try {
            MotorcycleResponse updatedMotorcycle = motorcycleService.updateMotorcycle(id, motorcycle);
            return ResponseEntity.ok(ApiResponse.success("อัพเดทข้อมูลรถสำเร็จ", updatedMotorcycle));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/admin/motorcycles/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMotorcycle(@PathVariable String id) {
        try {
            motorcycleService.deleteMotorcycle(id);
            return ResponseEntity.ok(ApiResponse.success("ลบข้อมูลรถสำเร็จ", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== RESERVATIONS ====================
    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestBody ReservationRequest request,
            @RequestHeader("X-User-ID") String customerId) {
        try {
            ReservationResponse reservation = reservationService.createReservation(request, customerId);
            return ResponseEntity.ok(ApiResponse.success("จองรถสำเร็จ", reservation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable String id) {
        try {
            ReservationResponse reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(ApiResponse.success(reservation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/reservations/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationsByCustomer(@PathVariable String customerId) {
        try {
            List<ReservationResponse> reservations = reservationService.getReservationsByCustomer(customerId);
            return ResponseEntity.ok(ApiResponse.success(reservations));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservations() {
        try {
            List<ReservationResponse> reservations = reservationService.getAllReservations();
            return ResponseEntity.ok(ApiResponse.success(reservations));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/admin/reservations/{id}/status")
    public ResponseEntity<ApiResponse<ReservationResponse>> updateReservationStatus(
            @PathVariable String id, @RequestParam String status) {
        try {
            ReservationResponse reservation = reservationService.updateReservationStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("อัพเดทสถานะการจองสำเร็จ", reservation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/reservations/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelReservation(@PathVariable String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok(ApiResponse.success("ยกเลิกการจองสำเร็จ", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/reservations/calculate-price")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculatePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam BigDecimal pricePerDay,
            @RequestParam(required = false) String discountCode) {
        try {
            BigDecimal finalPrice = reservationService.calculatePrice(startDate, endDate, pricePerDay, discountCode);
            return ResponseEntity.ok(ApiResponse.success(Map.of("finalPrice", finalPrice)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== PAYMENTS ====================
    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        try {
            PaymentResponse payment = paymentService.processPayment(request);
            return ResponseEntity.ok(ApiResponse.success("สร้างการชำระเงินสำเร็จ", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/payments/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByReservationId(@PathVariable String reservationId) {
        try {
            PaymentResponse payment = paymentService.getPaymentByReservationId(reservationId);
            return ResponseEntity.ok(ApiResponse.success(payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/admin/payments/{paymentId}/status")
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePaymentStatus(
            @PathVariable String paymentId, @RequestParam String status) {
        try {
            PaymentResponse payment = paymentService.updatePaymentStatus(paymentId, status);
            return ResponseEntity.ok(ApiResponse.success("อัพเดทสถานะการชำระเงินสำเร็จ", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/payments/{reservationId}/upload-slip")
    public ResponseEntity<ApiResponse<String>> uploadPaymentSlip(
            @PathVariable String reservationId,
            @RequestParam("slipImage") MultipartFile slipImage) {
        try {
            paymentService.uploadPaymentSlip(reservationId, slipImage);
            return ResponseEntity.ok(ApiResponse.success("อัพโหลดสลิปสำเร็จ", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== USERS ====================
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        try {
            UserResponse user = userService.getUserByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllCustomers() {
        try {
            List<UserResponse> customers = userService.getAllCustomers();
            return ResponseEntity.ok(ApiResponse.success(customers));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/admin/employees")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllEmployees() {
        try {
            List<UserResponse> employees = userService.getAllEmployees();
            return ResponseEntity.ok(ApiResponse.success(employees));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/admin/employees")
    public ResponseEntity<ApiResponse<UserResponse>> createEmployee(
            @RequestBody UserRequest request,
            @RequestHeader("X-User-ID") String createdBy) {
        try {
            UserResponse employee = userService.createEmployee(request, createdBy);
            return ResponseEntity.ok(ApiResponse.success("สร้างพนักงานสำเร็จ", employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String id, @RequestBody UserRequest request) {
        try {
            UserResponse user = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("อัพเดทข้อมูลผู้ใช้สำเร็จ", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@PathVariable String id, @RequestBody UserRequest request) {
        try {
            UserResponse user = userService.updateProfile(id, request);
            return ResponseEntity.ok(ApiResponse.success("อัพเดทโปรไฟล์สำเร็จ", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== DISCOUNTS ====================
    @GetMapping("/discounts")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getAllActiveDiscounts() {
        try {
            List<DiscountResponse> discounts = discountService.getAllActiveDiscounts();
            return ResponseEntity.ok(ApiResponse.success(discounts));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/discounts/{code}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getDiscountByCode(@PathVariable String code) {
        try {
            DiscountResponse discount = discountService.getDiscountByCode(code);
            return ResponseEntity.ok(ApiResponse.success(discount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/admin/discounts")
    public ResponseEntity<ApiResponse<DiscountResponse>> createDiscount(
            @RequestBody DiscountRequest request,
            @RequestHeader("X-User-ID") String createdBy) {
        try {
            DiscountResponse discount = discountService.createDiscount(request, createdBy);
            return ResponseEntity.ok(ApiResponse.success("สร้างส่วนลดสำเร็จ", discount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/admin/discounts/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> updateDiscount(@PathVariable String id, @RequestBody DiscountRequest request) {
        try {
            DiscountResponse discount = discountService.updateDiscount(id, request);
            return ResponseEntity.ok(ApiResponse.success("อัพเดทส่วนลดสำเร็จ", discount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/admin/discounts/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateDiscount(@PathVariable String id) {
        try {
            discountService.deactivateDiscount(id);
            return ResponseEntity.ok(ApiResponse.success("ปิดใช้งานส่วนลดสำเร็จ", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== DASHBOARD ====================
    @GetMapping("/admin/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getAdminDashboardStats() {
        try {
            DashboardStatsResponse stats = dashboardService.getAdminDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/employee/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getEmployeeDashboardStats(@RequestHeader("X-User-ID") String employeeId) {
        try {
            DashboardStatsResponse stats = dashboardService.getEmployeeDashboardStats(employeeId);
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/admin/dashboard/recent-activities")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRecentActivities(@RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Object> activities = dashboardService.getRecentActivities(limit);
            return ResponseEntity.ok(ApiResponse.success(activities));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/admin/dashboard/revenue-report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueReport(@RequestParam String period) {
        try {
            Map<String, Object> report = dashboardService.getRevenueReport(period);
            return ResponseEntity.ok(ApiResponse.success(report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}