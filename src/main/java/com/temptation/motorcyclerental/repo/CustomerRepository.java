package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, String> {
    Optional<Customers> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByIdCardNumber(String idCardNumber);
    boolean existsByLicenseNumber(String licenseNumber);
}