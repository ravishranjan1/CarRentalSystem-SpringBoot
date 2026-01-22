package com.rentify.carrental.service;

import com.rentify.carrental.exception.PaymentNotFoundException;
import com.rentify.carrental.model.PaymentModel;

import java.util.List;

public interface PaymentService {

    void payment(PaymentModel paymentModel) throws Exception;
    List<PaymentModel> findAll();
    PaymentModel findById(Long id) throws PaymentNotFoundException;
    void removeById(Long id) throws Exception;
}
