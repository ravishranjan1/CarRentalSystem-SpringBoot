package com.rentify.carrental.service.impl;

import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.repo.CustomerRepo;
import com.rentify.carrental.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public void add(CustomerModel customerModel) throws Exception {
        try{
            if (customerModel.getName() == null) {
                throw new Exception("Name cannot be null");
            }
            if (customerModel.getPhone() == null) {
                throw new Exception("Phone number cannot be null");
            }
            if (customerModel.getDrivingLicenseNo() == null) {
                throw new Exception("Driving License No cannot be null");
            }
            if (customerModel.getPhone() < 1000000000L || customerModel.getPhone() > 9999999999L) {
                throw new Exception("Phone number must be exactly 10 digits");
            }
            customerModel.setName(customerModel.getName().toUpperCase());
            customerRepo.save(customerModel);
        }catch(Exception e){
            throw new Exception("Error while adding customer "+ e.getMessage());
        }
    }

    @Override
    public void update(CustomerModel customerModel) throws Exception {
        Optional<CustomerModel> opt = customerRepo.findById(customerModel.getId());
        if(opt.isPresent()){
            CustomerModel updateCustomer = opt.get();

            if (customerModel.getName() == null) {
                throw new Exception("Name cannot be null");
            }
            if (customerModel.getPhone() == null) {
                throw new Exception("Phone number cannot be null");
            }
            if (customerModel.getDrivingLicenseNo() == null) {
                throw new Exception("Driving License No cannot be null");
            }
            if (customerModel.getPhone() < 1000000000L || customerModel.getPhone() > 9999999999L) {
                throw new Exception("Phone number must be exactly 10 digits");
            }
            customerModel.setName(customerModel.getName().toUpperCase());
            updateCustomer.setName(customerModel.getName());
            updateCustomer.setPhone(customerModel.getPhone());
            updateCustomer.setDrivingLicenseNo(customerModel.getDrivingLicenseNo());

            customerRepo.save(updateCustomer);
        }else{
            throw new CustomerNotFoundException("Customer not found with given Id : "+customerModel.getId());
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
            throw new Exception("Error while deleting customer "+e.getMessage());
        }
    }
}
