package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees, String> {
    Optional<Employees> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Employees> findByOwnerId(String ownerId);
    List<Employees> findByIsActiveTrue();
    List<Employees> findByOwnerIdAndIsActiveTrue(String ownerId);
}