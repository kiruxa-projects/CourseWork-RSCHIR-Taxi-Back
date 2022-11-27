package com.repositories;

import com.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car findCarByDriverId(long driverId);
    Car findCarById(long id);
    List<Car> findCarsByDriverId(long driverId);

    void deleteCarByDriverId(long driverId);

    @Query("SELECT MAX(id) FROM Order")
    Long getMaxId();
}

