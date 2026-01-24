package com.rentify.carrental.service;

import com.rentify.carrental.exception.CarNotFoundException;
import com.rentify.carrental.exception.CompanyNotFoundException;
import com.rentify.carrental.model.CarModel;

import java.util.List;

public interface CarService {

    void save(CarModel carModel) throws Exception;
    void removeById(Long id) throws Exception;

    List<CarModel> findAll();
    CarModel findById(Long id) throws CarNotFoundException;
}
