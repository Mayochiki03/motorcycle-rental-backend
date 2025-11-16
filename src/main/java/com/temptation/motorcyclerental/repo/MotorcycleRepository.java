package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Motorcycles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycles, String> {
    Optional<Motorcycles> findByLicensePlate(String licensePlate);
    List<Motorcycles> findByBrand(String brand);
    List<Motorcycles> findByIsAvailableTrue();
    List<Motorcycles> findByBrandAndModelContainingIgnoreCase(String brand, String model);

    @Query("SELECT m FROM Motorcycles m WHERE m.isAvailable = true AND m.pricePerDay BETWEEN :minPrice AND :maxPrice")
    List<Motorcycles> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    List<Motorcycles> findByIsAvailable(Boolean isAvailable);
    List<Motorcycles> findByBrandAndIsAvailable(String brand, Boolean isAvailable);
    boolean existsByLicensePlate(String licensePlate);
}