package com.rentify.carrental.service.impl;

import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.repo.CustomerRepo;
import com.rentify.carrental.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerModel save(CustomerModel customerModel) throws Exception {
        try {
            customerModel.setName(customerModel.getName().toUpperCase());
            if(customerModel.getId() == null){
                customerModel.setPassword(passwordEncoder.encode(customerModel.getPassword()));
                return customerRepo.save(customerModel);
            }
            else {
                Optional<CustomerModel> opt = customerRepo.findById(customerModel.getId());
                if(opt.isEmpty()) {
                    throw new CustomerNotFoundException("Customer not found with ID: " + customerModel.getId());
                }
                CustomerModel updateCustomer = opt.get();
                updateCustomer.setName(customerModel.getName().toUpperCase());
                updateCustomer.setPhone(customerModel.getPhone());
                updateCustomer.setDrivingLicenseNo(customerModel.getDrivingLicenseNo());
                updateCustomer.setEmail(customerModel.getEmail());
                updateCustomer.setUsername(customerModel.getUsername());
                updateCustomer.setRole(customerModel.getRole());
                if(customerModel.getPassword() != null && !customerModel.getPassword().trim().isEmpty()) {
                    updateCustomer.setPassword(passwordEncoder.encode(customerModel.getPassword()));
                }
                return customerRepo.save(updateCustomer);
            }
        }
        catch(CustomerNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new Exception("Error while saving the data. " + "Please check the given data.");
        }
    }


    @Override
    public List<CustomerModel> findAll() {
         return customerRepo.findAll();
    }

    @Override
    public CustomerModel findById(Long id) throws CustomerNotFoundException {
        Optional<CustomerModel> opt =customerRepo.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new CustomerNotFoundException("Customer not found with given Id : "+id);
        }
    }

    @Override
    public void removeById(Long id) throws Exception {
        try{
            customerRepo.deleteById(id);
        }catch(Exception e){
            throw new Exception("Error while deleting customer ");
        }
    }

    @Override
    public CustomerModel findByUsername(String username) {
        Optional<CustomerModel> customer = customerRepo.findByUsername(username);
        if(customer.isPresent()){
            return customer.get();
        }
        return null;
    }
}
