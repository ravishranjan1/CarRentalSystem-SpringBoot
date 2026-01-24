package com.rentify.carrental.service;

import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.model.BookingModel;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    void rentCar(BookingModel bookingModel) throws Exception;
    BookingModel returnCar(BookingModel bookingModel) throws Exception;
    BookingModel findById(Long id) throws BookingNotFoundException;
    void removeById(Long id) throws Exception;
    List<BookingModel> findAll();
}
