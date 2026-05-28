package com.rentify.carrental.validators;

import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        // NULL CHECK

        if (booking == null) {

            errors.add("Booking data cannot be null");

            return errors;
        }

        // CUSTOMER VALIDATION

        if (booking.getCustomer() == null) {

            errors.add("Customer cannot be null");

        } else if (booking.getCustomer().getId() == null) {

            errors.add("Customer ID cannot be null");
        }

        // CAR VALIDATION

        if (booking.getCar() == null) {

            errors.add("Car cannot be null");

        } else if (booking.getCar().getId() == null) {

            errors.add("Car ID cannot be null");
        }

        // START DATETIME VALIDATION

        if (booking.getStartDateTime() == null) {

            errors.add("Start date & time cannot be null");
        }

        // END DATETIME VALIDATION

        if (booking.getEndDateTime() == null) {

            errors.add("End date & time cannot be null");
        }

        // CURRENT DATETIME

        LocalDateTime now = LocalDateTime.now();

        // PAST DATETIME CHECK

        if (booking.getStartDateTime() != null && booking.getStartDateTime().isBefore(now)) {

            errors.add("Start date & time must be current or future");
        }

        // END BEFORE START CHECK

        if (booking.getStartDateTime() != null && booking.getEndDateTime() != null && booking.getEndDateTime().isBefore(booking.getStartDateTime())) {

            errors.add("End date & time cannot be before start date & time");
        }

        // SAME START & END CHECK

        if (booking.getStartDateTime() != null && booking.getEndDateTime() != null && booking.getEndDateTime().equals(booking.getStartDateTime())) {

            errors.add("Booking duration must be at least 1 hour");
        }
        // CAR AVAILABILITY CHECK
        if (booking.getCar() != null && booking.getCar().getId() != null && booking.getStartDateTime() != null && booking.getEndDateTime() != null) {

            boolean available;

            if (booking.getId() == null) {

                // NEW BOOKING

                available = bookingService.isCarAvailable(booking.getCar().getId(), booking.getStartDateTime(), booking.getEndDateTime());
            } else {

                // EDIT BOOKING
                available = bookingService.isCarAvailableForEdit(booking.getId(), booking.getCar().getId(), booking.getStartDateTime(), booking.getEndDateTime());
            }

            if (!available) {

                errors.add("Car is not available for selected date & time");
            }

            if (!available) {

                errors.add("Car is not available for selected date & time");
            }
        }
        LocalDateTime start = booking.getStartDateTime();
        LocalDateTime end = booking.getEndDateTime();
        long hours = ChronoUnit.HOURS.between(start, end);
        if (hours < 1) {
            errors.add("Minimum booking time is 1 hour");
        }

        return errors;
    }
}