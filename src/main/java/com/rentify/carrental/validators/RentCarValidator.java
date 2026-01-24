package com.rentify.carrental.validators;

import com.rentify.carrental.model.BookingModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RentCarValidator implements DataValidator{
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
            errors.add("Pickup date cannot be null");
        }

        return errors;
    }
}
