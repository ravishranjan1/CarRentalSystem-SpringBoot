package com.rentify.carrental.service;

import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.CustomerModel;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerModel save(CustomerModel customerModel) throws Exception;

    List<CustomerModel> findAll();
    CustomerModel findById(Long id) throws CustomerNotFoundException;

    void removeById(Long id) throws Exception;

    CustomerModel findByUsername(String username);
}
