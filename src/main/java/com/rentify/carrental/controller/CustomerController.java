package com.rentify.carrental.controller;


import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.service.CustomerService;
import com.rentify.carrental.validators.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rentify/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerValidator validator;

    @GetMapping("/")
    public String getCustomer(Model model){
        List<CustomerModel> customers = customerService.findAll();
        if(customers.isEmpty()){
            model.addAttribute("error", "No customer found");
        }else{
            model.addAttribute("success", customers.size() + " Customers found");
        }
        model.addAttribute("customers", customers);
        return "customer";
    }

    @GetMapping("/new")
    public String createCustomer(Model model){
        model.addAttribute("customer", new CustomerModel());
        return "customer-form";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute CustomerModel customer, Model model){
        List<String> errors = validator.validate(customer);
        if(!errors.isEmpty()){
            model.addAttribute("error", errors);
        }else{
            try {
                customerService.save(customer);
                if(customer.getId() == null){
                    model.addAttribute("success", "Customer added success fully");
                }else{
                    model.addAttribute("success", "Customer updated success fully");
                }
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("customers", customerService.findAll());
        return "customer";
    }

    @GetMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id, Model model){
        try{
            CustomerModel customer = customerService.findById(id);
            model.addAttribute("customer", customer);
            return "customer-form";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("error", "No customer found with given id : "+id);
        }
        return "customer";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model){
        try {
            customerService.removeById(id);
            model.addAttribute("success", "Customer deleted success");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("customers", customerService.findAll());
        return "customer";
    }
}
