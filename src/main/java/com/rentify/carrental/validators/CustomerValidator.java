package com.rentify.carrental.validators;

import com.rentify.carrental.model.CustomerModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerValidator implements DataValidator {
    @Override
    public List<String> validate(Object data) {
        List<String> errors = new ArrayList<String>();
        CustomerModel customer = (CustomerModel) data;

        if (customer.getName() == null) {
            errors.add("Name cannot be null");
        }
        if (customer.getPhone() == null) {
            errors.add("Phone number cannot be null");
        }
        if (customer.getDrivingLicenseNo() == null) {
            errors.add("Driving License No cannot be null");
        }
        if (customer.getPhone() < 1000000000L || customer.getPhone() > 9999999999L) {
            errors.add("Phone number must be exactly 10 digits");
        }
        return errors;
    }
}
