package com.rentify.carrental.service;

import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.model.BookingModel;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    void booking(BookingModel bookingModel) throws Exception;
    List<BookingModel> rentCarsForToday()throws Exception;
    List<BookingModel> returnCars() throws Exception;
    BookingModel findById(Long id) throws BookingNotFoundException;
    void removeById(Long id) throws Exception;
    List<BookingModel> findAll();

    boolean isCarAvailable(Long carId, LocalDate from, LocalDate to);
}
