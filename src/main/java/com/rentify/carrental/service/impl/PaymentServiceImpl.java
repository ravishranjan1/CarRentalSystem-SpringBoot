package com.rentify.carrental.service.impl;

import com.rentify.carrental.enums.PaymentStatus;
import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.exception.PaymentNotFoundException;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.model.PaymentModel;
import com.rentify.carrental.repo.PaymentRepo;
import com.rentify.carrental.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Override
    public void payment(PaymentModel paymentModel) throws Exception {
        try{
            paymentModel.setStatus(PaymentStatus.SUCCESS);
            paymentRepo.save(paymentModel);
        }catch(Exception e){
            throw new Exception("Payment not Completed ! TRY AGAIN");
        }
    }

    @Override
    public List<PaymentModel> findAll() {
        return paymentRepo.findAll();
    }

    @Override
    public PaymentModel findById(Long id) throws PaymentNotFoundException {
        Optional<PaymentModel> opt =paymentRepo.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new PaymentNotFoundException("Payment not found with given Id : "+id);
        }
    }

    @Override
    public void removeById(Long id) throws Exception {
        try{
            paymentRepo.deleteById(id);
        }catch(Exception e){
            throw new Exception("Error while deleting customer ");
        }
    }
}
