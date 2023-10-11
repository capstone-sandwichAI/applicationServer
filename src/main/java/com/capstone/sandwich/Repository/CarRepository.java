package com.capstone.sandwich.Repository;

import com.capstone.sandwich.Domain.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Integer> {
}
