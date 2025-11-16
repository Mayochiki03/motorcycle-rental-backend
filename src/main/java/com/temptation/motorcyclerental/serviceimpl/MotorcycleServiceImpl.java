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
        Motorcycles motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลรถ"));
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

        // เริ่มจากข้อมูลทั้งหมด
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

        System.out.println("Found " + motorcycles.size() + " motorcycles after filtering");
        return motorcycles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MotorcycleResponse> getAvailableMotorcycles(LocalDate startDate, LocalDate endDate) {
        List<Motorcycles> motorcycles = motorcycleCustomRepository.findAvailableMotorcycles(startDate, endDate);
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

        // Update fields
        existingMotorcycle.setBrand(motorcycle.getBrand());
        existingMotorcycle.setModel(motorcycle.getModel());
        existingMotorcycle.setYear(motorcycle.getYear());
        existingMotorcycle.setColor(motorcycle.getColor());
        existingMotorcycle.setEngineCc(motorcycle.getEngineCc());
        existingMotorcycle.setPricePerDay(motorcycle.getPricePerDay());
        existingMotorcycle.setImageUrl(motorcycle.getImageUrl());
        existingMotorcycle.setDescription(motorcycle.getDescription());
        existingMotorcycle.setMaintenanceStatus(motorcycle.getMaintenanceStatus());
        existingMotorcycle.setIsAvailable(motorcycle.getIsAvailable());

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