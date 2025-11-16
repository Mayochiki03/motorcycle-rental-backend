package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.domain.Motorcycles;
import com.temptation.motorcyclerental.obj.request.MotorcycleSearchRequest;
import com.temptation.motorcyclerental.obj.response.MotorcycleResponse;

import java.time.LocalDate;
import java.util.List;

public interface MotorcycleService {
    List<MotorcycleResponse> getAllMotorcycles();
    MotorcycleResponse getMotorcycleById(String id);
    List<MotorcycleResponse> searchMotorcycles(MotorcycleSearchRequest request);
    List<MotorcycleResponse> getAvailableMotorcycles(LocalDate startDate, LocalDate endDate);
    MotorcycleResponse createMotorcycle(Motorcycles motorcycle);
    MotorcycleResponse updateMotorcycle(String id, Motorcycles motorcycle);
    void deleteMotorcycle(String id);
    boolean isMotorcycleAvailable(String motorcycleId, LocalDate startDate, LocalDate endDate);
}