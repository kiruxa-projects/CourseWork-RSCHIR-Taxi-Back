package com.controllers;

import com.security.JWebToken;
import com.TokenManager;
import com.models.Car;
import com.services.CarService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/car")
public class CarController {
    CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(value = "")
    public ResponseEntity<HashMap> getAllCars(String token) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        HashMap cars = (HashMap) carService.getAllCars();
        HashMap res = new HashMap();
        res.put("response", cars);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HashMap> getCarById(String token, Long id) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Car car = carService.getCarById(id);
        HashMap res = new HashMap();
        res.put("response", car);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @PostMapping(value="")
    public ResponseEntity<HashMap> addCar(String token, String model, String color, String number, String type, String price) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Car car = new Car();
        car.setId(carService.getNextId());
        car.setModel(model);
        car.setColor(color);
        car.setNumber(number);
        carService.saveCar(car);
        HashMap res = new HashMap();
        res.put("response", car);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HashMap> deleteCar(String token, Long id) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        carService.deleteCarById(id);
        HashMap res = new HashMap();
        res.put("response", "Car deleted");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

