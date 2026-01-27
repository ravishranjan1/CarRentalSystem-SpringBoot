package com.rentify.carrental.validators;

import com.rentify.carrental.enums.CarStatus;
import com.rentify.carrental.model.BookingModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RentCarValidator implements DataValidator{
    @Override
    public List<String> validate(Object data) {

        List<String> errors = new ArrayList<>();

        if (!(data instanceof BookingModel)) {
            errors.add("Invalid booking data");
        }

        BookingModel booking = (BookingModel) data;

        if (booking.getStatus() == null) {
            errors.add("Booking status cannot be null");
        }

        if (booking.getStatus() != CarStatus.SCHEDULED) {
            errors.add("Car can be rented only for scheduled bookings");
        }

        return errors;
    }
}
