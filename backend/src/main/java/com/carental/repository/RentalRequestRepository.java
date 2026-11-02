package com.carental.repository;

import com.carental.model.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
}
