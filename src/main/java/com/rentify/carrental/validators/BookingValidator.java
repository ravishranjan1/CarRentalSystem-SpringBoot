package com.rentify.carrental.validators;

import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingValidator implements DataValidator {

    @Autowired
    private BookingService bookingService;

    @Override
    public List<String> validate(Object data) {

        List<String> errors = new ArrayList<>();
        BookingModel booking = (BookingModel) data;

        if (booking == null) {
            errors.add("Booking data cannot be null");
            return errors;
        }

        if (booking.getCustomer() == null) {
            errors.add("Customer cannot be null");
        } else if (booking.getCustomer().getId() == null) {
            errors.add("Customer Id cannot be null");
        }

        if (booking.getCar() == null) {
            errors.add("Car cannot be null");
        } else if (booking.getCar().getId() == null) {
            errors.add("Car Id cannot be null");
        }

        if (booking.getStartDate() == null) {
            errors.add("Start date cannot be null");
        }

        if (booking.getEndDate() == null) {
            errors.add("Return date cannot be null");
        }

        if (booking.getStartDate() != null &&
                booking.getStartDate().isBefore(LocalDate.now())) {
            errors.add("Start date must be today or a future date");
        }

        if (booking.getStartDate() != null && booking.getEndDate() != null &&
                booking.getEndDate().isBefore(booking.getStartDate())) {
            errors.add("Return date cannot be before pickup date");
        }

        if (booking.getCar() != null && booking.getCar().getId() != null && booking.getStartDate() != null && booking.getEndDate() != null) {
            boolean available = bookingService.isCarAvailable(booking.getCar().getId(),booking.getStartDate(), booking.getEndDate());
            if (!available) {
                errors.add("Car is not available for selected dates");
            }
        }
        return errors;
    }
}

