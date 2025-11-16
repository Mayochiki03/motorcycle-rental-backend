package com.temptation.motorcyclerental.repocustom;

import com.temptation.motorcyclerental.domain.Motorcycles;
import com.temptation.motorcyclerental.objc.MotorcycleSearchCriteria;

import java.time.LocalDate;
import java.util.List;

public interface MotorcycleCustomRepository {
    List<Motorcycles> searchMotorcycles(MotorcycleSearchCriteria criteria); // ← ต้องมี parameter นี้
    List<Motorcycles> findAvailableMotorcycles(LocalDate startDate, LocalDate endDate);
    List<Object[]> getMotorcycleUsageStats(LocalDate startDate, LocalDate endDate);
}