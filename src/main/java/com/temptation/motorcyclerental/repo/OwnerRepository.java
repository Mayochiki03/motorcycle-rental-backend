package com.temptation.motorcyclerental.repo;

import com.temptation.motorcyclerental.domain.Owners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owners, String> {
    Optional<Owners> findByEmail(String email);
    boolean existsByEmail(String email);
}