package com.rentify.carrental.validators;

import com.rentify.carrental.enums.CarStatus;
import com.rentify.carrental.model.BookingModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReturnCarValidator implements DataValidator{
    @Override
    public List<String> validate(Object data) {

        List<String> errors = new ArrayList<>();
        BookingModel booking = (BookingModel) data;

        if (booking == null) {
            errors.add("Booking data cannot be null");
            return errors;
        }

        if (booking.getId() == null) {
            errors.add("Booking Id cannot be null");
        }

        if (booking.getEndDate() == null) {
            errors.add("Return date cannot be null");
        }

        if (booking.getStartDate() != null && booking.getEndDate() != null) {
            if (booking.getEndDate().isBefore(booking.getStartDate())) {
                errors.add("Return date cannot be before pickup date");
            }
        }

        if (!CarStatus.ONGOING.equals(booking.getStatus())) {
            errors.add("Car already returned or not rented yet");
        }

        return errors;
    }
}
