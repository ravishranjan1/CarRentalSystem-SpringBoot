package com.rentify.carrental.repo;

import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<BookingModel, Long> {
    List<BookingModel> findByCustomer(CustomerModel customer);
}
