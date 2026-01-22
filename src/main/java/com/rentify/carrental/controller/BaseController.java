package com.rentify.carrental.controller;

import com.rentify.carrental.model.PaymentModel;
import com.rentify.carrental.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/rentify")
public class BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String getHome(Model model){
        List<PaymentModel> paymentList = paymentService.findAll();
        BigDecimal totalAmount = BigDecimal.valueOf(paymentList.stream().mapToDouble(PaymentModel::getAmount).sum());

        model.addAttribute("totalCustomers", customerService.findAll().size());
        model.addAttribute("totalCars", carService.findAll().size());
        model.addAttribute("totalBookings", bookingService.findAll().size());
        model.addAttribute("totalPayments", totalAmount);
        return "home";
    }
}
