package com.rentify.carrental.validators;

import com.rentify.carrental.model.CompanyModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyValidator implements DataValidator{
    @Override
    public List<String> validate(Object data) {
        List<String> errors = new ArrayList<>();
        CompanyModel company = (CompanyModel) data;

        if(company.getName() == null){
            errors.add("Name cannot be null");
        }
        if(company.getCountry() == null){
            errors.add("Country cannot be null");
        }

        return errors;
    }
}
