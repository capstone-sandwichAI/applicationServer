package com.capstone.sandwich.Repository;

import com.capstone.sandwich.Domain.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {
    Optional<Car> findByCarNumber(String carNumber);
}
