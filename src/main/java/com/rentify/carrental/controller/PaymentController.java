package com.rentify.carrental.controller;

import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.exception.PaymentNotFoundException;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.model.PaymentModel;
import com.rentify.carrental.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rentify/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String getAllPaymentDetails(Model model){
        List<PaymentModel> payments = paymentService.findAll();
        if(payments.isEmpty()){
            model.addAttribute("error", "No payment found");
        }else{
            model.addAttribute("success", payments.size() + " Payment found");
        }
        model.addAttribute("payments", payments);
        return "payment";
    }

    @GetMapping("/find/{id}")
    public String getPaymentById(@PathVariable Long id, Model model){
        try {
            PaymentModel payment = paymentService.findById(id);
            model.addAttribute("success", "Payment found");
            model.addAttribute("payments", List.of(payment));
        }  catch (PaymentNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("payments", null);
        }
        return "payment";
    }

}
