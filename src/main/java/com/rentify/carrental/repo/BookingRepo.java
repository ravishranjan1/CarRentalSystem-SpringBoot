package com.rentify.carrental.repo;

import com.rentify.carrental.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<BookingModel, Long> {
}
