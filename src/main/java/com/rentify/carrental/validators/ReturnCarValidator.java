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

        if (!(data instanceof BookingModel)) {
            errors.add("Invalid booking data");
            return errors;
        }

        BookingModel booking = (BookingModel) data;

        if (booking.getId() == null) {
            errors.add("Booking Id cannot be null");
        }

        if (booking.getStatus() == null) {
            errors.add("Booking status cannot be null");
            return errors;
        }

        if (booking.getStatus() != CarStatus.ONGOING) {
            errors.add("Car can be returned only for ongoing rentals");
        }

        return errors;
    }

}
