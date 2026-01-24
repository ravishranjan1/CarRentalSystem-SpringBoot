package com.rentify.carrental.validators;

import com.rentify.carrental.model.CarModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarValidator implements DataValidator{

    @Override
    public List<String> validate(Object data) {
        List<String> errors = new ArrayList<>();
        CarModel car = (CarModel) data;

        if (car == null) {
            errors.add("Car data cannot be null");
        }

        if (car.getCompany() == null || car.getCompany().getId() == null) {
            errors.add("Company is required");
        }

        if(car.getModel() == null){
            errors.add("Model cannot be null");
        }

        if(car.getRegistrationNo() == null){
            errors.add("RegistrationNo cannot be null");
        }

        return errors;
    }
}
