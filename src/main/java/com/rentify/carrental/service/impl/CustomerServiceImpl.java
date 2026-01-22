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
    public void save(CustomerModel customerModel) throws Exception {
        try{
            customerModel.setName(customerModel.getName().toUpperCase());

            if(customerModel.getId() == null){
                customerRepo.save(customerModel);
            }else{
                Optional<CustomerModel> opt = customerRepo.findById(customerModel.getId());

                if(opt.isEmpty()){
                    throw new CustomerNotFoundException("Customer not found with ID: " + customerModel.getId());
                }
                CustomerModel updateCustomer = opt.get();

                updateCustomer.setName(customerModel.getName());
                updateCustomer.setPhone(customerModel.getPhone());
                updateCustomer.setDrivingLicenseNo(customerModel.getDrivingLicenseNo());

                customerRepo.save(updateCustomer);
            }
        }catch(CustomerNotFoundException e){
            throw e;
        }
        catch (Exception e) {
            throw new Exception("Error while Saving the data please check the given data is correct or not");
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
}
