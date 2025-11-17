package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.Motorcycles;
import com.temptation.motorcyclerental.domain.Motorcycles;
import com.temptation.motorcyclerental.obj.request.MotorcycleSearchRequest;
import com.temptation.motorcyclerental.obj.response.MotorcycleResponse;
import com.temptation.motorcyclerental.objc.MotorcycleSearchCriteria;
import com.temptation.motorcyclerental.repo.MotorcycleRepository;
import com.temptation.motorcyclerental.repocustom.MotorcycleCustomRepository;
import com.temptation.motorcyclerental.service.MotorcycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotorcycleServiceImpl implements MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final MotorcycleCustomRepository motorcycleCustomRepository;

    @Override
    public List<MotorcycleResponse> getAllMotorcycles() {
        return motorcycleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MotorcycleResponse getMotorcycleById(String id) {
        System.out.println(" SERVICE: Searching for motorcycle ID: " + id);

        //  ตรวจสอบก่อนว่า repository มีข้อมูลอะไรบ้าง
        List<Motorcycles> allMotorcycles = motorcycleRepository.findAll();
        System.out.println(" All motorcycles in repository:");
        allMotorcycles.forEach(m ->
                System.out.println("  - " + m.getMotorcycleId() + " : " + m.getBrand() + " " + m.getModel())
        );

        // ตรวจสอบว่า ID นี้มีอยู่ใน repository หรือไม่
        boolean exists = motorcycleRepository.existsById(id);
        System.out.println(" Motorcycle exists by ID '" + id + "': " + exists);

        //  ลองหาโดยตรง
        Optional<Motorcycles> motorcycleOpt = motorcycleRepository.findById(id);

        if (motorcycleOpt.isEmpty()) {
            System.out.println(" ERROR: Motorcycle not found with ID: " + id);
            System.out.println(" Available IDs: " +
                    allMotorcycles.stream()
                            .map(Motorcycles::getMotorcycleId)
                            .collect(Collectors.toList())
            );
            throw new RuntimeException("ไม่พบข้อมูลรถด้วย ID: " + id);
        }

        Motorcycles motorcycle = motorcycleOpt.get();
        System.out.println(" SUCCESS: Found motorcycle: " + motorcycle.getMotorcycleId());
        System.out.println(" Image URL: " + motorcycle.getImageUrl());

        return convertToResponse(motorcycle);
    }

    @Override
    public List<MotorcycleResponse> searchMotorcycles(MotorcycleSearchRequest request) {
        System.out.println("=== SERVICE: SEARCH MOTORCYCLES ===");
        System.out.println("Brand: " + request.getBrand());
        System.out.println("Model: " + request.getModel());
        System.out.println("IsAvailable: " + request.getIsAvailable());
        System.out.println("MinPrice: " + request.getMinPrice());
        System.out.println("MaxPrice: " + request.getMaxPrice());
        System.out.println("MinCC: " + request.getMinCC());
        System.out.println("MaxCC: " + request.getMaxCC());
        System.out.println("StartDate: " + request.getStartDate());
        System.out.println("EndDate: " + request.getEndDate());

        // ถ้ามีวันที่ ให้เช็ค availability ตามวันที่
        if (request.getStartDate() != null && request.getEndDate() != null) {
            System.out.println("✅ Using date-based availability check");
            List<Motorcycles> availableMotorcycles = motorcycleCustomRepository.findAvailableMotorcycles(
                    request.getStartDate(), request.getEndDate());

            // Filter ตาม criteria อื่นๆ จาก motorcycles ที่ว่างตามวันที่
            List<Motorcycles> filteredMotorcycles = availableMotorcycles.stream()
                    .filter(m -> request.getBrand() == null || request.getBrand().isEmpty() ||
                            m.getBrand().equalsIgnoreCase(request.getBrand()))
                    .filter(m -> request.getModel() == null || request.getModel().isEmpty() ||
                            m.getModel().toLowerCase().contains(request.getModel().toLowerCase()))
                    .filter(m -> request.getIsAvailable() == null || m.getIsAvailable().equals(request.getIsAvailable()))
                    .filter(m -> request.getMinPrice() == null || m.getPricePerDay().compareTo(request.getMinPrice()) >= 0)
                    .filter(m -> request.getMaxPrice() == null || m.getPricePerDay().compareTo(request.getMaxPrice()) <= 0)
                    .filter(m -> request.getMinCC() == null || (m.getEngineCc() != null && m.getEngineCc() >= request.getMinCC()))
                    .filter(m -> request.getMaxCC() == null || (m.getEngineCc() != null && m.getEngineCc() <= request.getMaxCC()))
                    .collect(Collectors.toList());

            System.out.println("Found " + filteredMotorcycles.size() + " available motorcycles after date filtering");
            return filteredMotorcycles.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }

        // ถ้าไม่มีวันที่ ให้ใช้ logic เดิม (เช็คแค่ isAvailable field)
        System.out.println("ℹ️ Using static availability check (no dates provided)");
        List<Motorcycles> motorcycles = motorcycleRepository.findAll();

        // Filter ตาม criteria ที่ส่งมา
        if (request.getBrand() != null && !request.getBrand().isEmpty()) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getBrand().equalsIgnoreCase(request.getBrand()))
                    .collect(Collectors.toList());
        }

        if (request.getModel() != null && !request.getModel().isEmpty()) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getModel().toLowerCase().contains(request.getModel().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (request.getIsAvailable() != null) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getIsAvailable().equals(request.getIsAvailable()))
                    .collect(Collectors.toList());
        }

        if (request.getMinPrice() != null) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getPricePerDay().compareTo(request.getMinPrice()) >= 0)
                    .collect(Collectors.toList());
        }

        if (request.getMaxPrice() != null) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getPricePerDay().compareTo(request.getMaxPrice()) <= 0)
                    .collect(Collectors.toList());
        }

        if (request.getMinCC() != null) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getEngineCc() != null && m.getEngineCc() >= request.getMinCC())
                    .collect(Collectors.toList());
        }

        if (request.getMaxCC() != null) {
            motorcycles = motorcycles.stream()
                    .filter(m -> m.getEngineCc() != null && m.getEngineCc() <= request.getMaxCC())
                    .collect(Collectors.toList());
        }

        System.out.println("Found " + motorcycles.size() + " motorcycles after static filtering");
        return motorcycles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MotorcycleResponse> getAvailableMotorcycles(LocalDate startDate, LocalDate endDate) {
        System.out.println("=== GET AVAILABLE MOTORCYCLES ===");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        List<Motorcycles> motorcycles = motorcycleCustomRepository.findAvailableMotorcycles(startDate, endDate);

        System.out.println("Found " + motorcycles.size() + " available motorcycles:");
        motorcycles.forEach(m ->
                System.out.println(" - " + m.getMotorcycleId() + ": " + m.getBrand() + " " + m.getModel())
        );

        return motorcycles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MotorcycleResponse createMotorcycle(Motorcycles motorcycle) {
        if (motorcycleRepository.existsByLicensePlate(motorcycle.getLicensePlate())) {
            throw new RuntimeException("ป้ายทะเบียนนี้ใช้งานแล้ว");
        }

        Motorcycles savedMotorcycle = motorcycleRepository.save(motorcycle);
        return convertToResponse(savedMotorcycle);
    }


    @Override
    public MotorcycleResponse updateMotorcycle(String id, Motorcycles motorcycle) {
        Motorcycles existingMotorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลรถ"));

        // อัพเดทเฉพาะ field ที่ไม่ null
        if (motorcycle.getBrand() != null) {
            existingMotorcycle.setBrand(motorcycle.getBrand());
        }
        if (motorcycle.getModel() != null) {
            existingMotorcycle.setModel(motorcycle.getModel());
        }
        if (motorcycle.getYear() != null) {
            existingMotorcycle.setYear(motorcycle.getYear());
        }
        if (motorcycle.getColor() != null) {
            existingMotorcycle.setColor(motorcycle.getColor());
        }
        if (motorcycle.getEngineCc() != null) {
            existingMotorcycle.setEngineCc(motorcycle.getEngineCc());
        }
        if (motorcycle.getPricePerDay() != null) {
            existingMotorcycle.setPricePerDay(motorcycle.getPricePerDay());
        }
        if (motorcycle.getImageUrl() != null) {
            existingMotorcycle.setImageUrl(motorcycle.getImageUrl());
        }
        if (motorcycle.getDescription() != null) {
            existingMotorcycle.setDescription(motorcycle.getDescription());
        }
        if (motorcycle.getMaintenanceStatus() != null) {
            existingMotorcycle.setMaintenanceStatus(motorcycle.getMaintenanceStatus());
        }
        if (motorcycle.getIsAvailable() != null) {
            existingMotorcycle.setIsAvailable(motorcycle.getIsAvailable());
        }

        Motorcycles updatedMotorcycle = motorcycleRepository.save(existingMotorcycle);
        return convertToResponse(updatedMotorcycle);
    }

    @Override
    public void deleteMotorcycle(String id) {
        Motorcycles motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลรถ"));
        motorcycleRepository.delete(motorcycle);
    }

    @Override
    public boolean isMotorcycleAvailable(String motorcycleId, LocalDate startDate, LocalDate endDate) {
        List<Motorcycles> availableMotorcycles = motorcycleCustomRepository.findAvailableMotorcycles(startDate, endDate);
        return availableMotorcycles.stream()
                .anyMatch(m -> m.getMotorcycleId().equals(motorcycleId));
    }

    private MotorcycleResponse convertToResponse(Motorcycles motorcycle) {
        MotorcycleResponse response = new MotorcycleResponse();
        response.setMotorcycleId(motorcycle.getMotorcycleId());
        response.setBrand(motorcycle.getBrand());
        response.setModel(motorcycle.getModel());
        response.setYear(motorcycle.getYear());
        response.setImageUrl(motorcycle.getImageUrl());
        response.setLicensePlate(motorcycle.getLicensePlate());
        response.setColor(motorcycle.getColor());
        response.setEngineCc(motorcycle.getEngineCc());
        response.setPricePerDay(motorcycle.getPricePerDay());
        response.setImageUrl(motorcycle.getImageUrl());
        response.setIsAvailable(motorcycle.getIsAvailable());
        response.setDescription(motorcycle.getDescription());
        response.setMaintenanceStatus(motorcycle.getMaintenanceStatus().name());
        response.setCreatedAt(motorcycle.getCreatedAt());
        return response;
    }
}