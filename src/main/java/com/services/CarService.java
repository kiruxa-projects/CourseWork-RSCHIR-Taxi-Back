package com.services;

import com.models.Car;
import com.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Slf4j
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void deleteCarById(Long id){
        carRepository.deleteById(id);
    }

    public void deleteCarByDriverId(Long id){
        carRepository.deleteCarByDriverId(id);
    }

    @Transactional
    public Long getNextId(){
        if(carRepository.getMaxId()==null){
            return 0L;
        }
        return (Long) (carRepository.getMaxId()+1);
    }

    @Transactional
    public void deleteCar(Car car){
        carRepository.delete(car);
    }

    public Car getCarByDriverId(Long id){
        return carRepository.findCarByDriverId(id);
    }

    public Car getCarById(Long id){
        return carRepository.findCarById(id);
    }

    public Car saveCar(Car car){
        return carRepository.save(car);
    }

    public Car updateCar(Car car){
        return carRepository.save(car);
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }
}
