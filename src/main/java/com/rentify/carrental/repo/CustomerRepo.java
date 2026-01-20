package com.rentify.carrental.repo;

import com.rentify.carrental.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerModel, Long> {
}
