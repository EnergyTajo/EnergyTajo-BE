package com.energy.tajo.bicycle.repository;

import com.energy.tajo.bicycle.domain.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BicycleRepository extends JpaRepository<Bicycle, String> {
    Bicycle findByBicycleId(String bicycleId);
}
