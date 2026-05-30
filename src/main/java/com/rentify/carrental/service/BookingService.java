package com.rentify.carrental.service;

import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CustomerModel;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingModel booking(BookingModel bookingModel) throws Exception;
    void autoUpdateBookingStatus() throws Exception;
    BookingModel findById(Long id) throws BookingNotFoundException;
    void removeById(Long id) throws Exception;
    List<BookingModel> findAll();

    boolean isCarAvailable(Long carId, LocalDateTime from, LocalDateTime to);

    List<BookingModel> findByCustomer(CustomerModel customer);

    BookingModel cancelBooking(Long id) throws Exception;

    BookingModel findByIdAndCustomer(Long bookingId, Long customerId);

    boolean isCarAvailableForEdit(Long bookingId, Long carId, LocalDateTime from, LocalDateTime to);
}
