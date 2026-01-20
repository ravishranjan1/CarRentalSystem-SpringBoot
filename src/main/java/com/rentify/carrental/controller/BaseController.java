package com.rentify.carrental.controller;

import com.rentify.carrental.service.CompanyService;
import com.rentify.carrental.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rentify")
public class BaseController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("totalCustomers", customerService.findAll().size());
        return "home";
    }
}
