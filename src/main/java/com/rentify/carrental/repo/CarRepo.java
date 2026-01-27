package com.rentify.carrental.repo;

import com.rentify.carrental.enums.CarType;
import com.rentify.carrental.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepo extends JpaRepository<CarModel, Long> {
    List<CarModel> findByType(CarType type);
}
