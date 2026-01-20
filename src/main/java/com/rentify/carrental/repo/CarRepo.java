package com.rentify.carrental.repo;

import com.rentify.carrental.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends JpaRepository<CarModel, Long> {
}
