package com.rentify.carrental.controller;

import com.rentify.carrental.enums.Role;
import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CarModel;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.model.PaymentModel;
import com.rentify.carrental.service.*;
import com.rentify.carrental.validators.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private CustomerValidator customerValidator;

    @GetMapping("/")
    public String getHome(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/rentify/login";
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            String username = authentication.getName();
            CustomerModel customer = customerService.findByUsername(username);
            if (customer == null) {
                model.addAttribute("error", "Customer account not found");
                return "access-denied";
            }
            List<BookingModel> bookingList = bookingService.findByCustomer(customer);
            List<CarModel> carList = carService.findAll();
            model.addAttribute("customer", customer);
            model.addAttribute("bookingList", bookingList);
            model.addAttribute("carList", carList);
            return "user-home";
        }

        if (authorities.stream()
                .anyMatch(a -> a.getAuthority()
                        .equals("ROLE_ADMIN"))) {

            String username =
                    authentication.getName();

            CustomerModel customer =
                    customerService.findByUsername(username);

            model.addAttribute("customer", customer);

            List<PaymentModel> paymentList =
                    paymentService.findAll();

            BigDecimal totalAmount = BigDecimal.valueOf(
                    paymentList.stream()
                            .mapToDouble(PaymentModel::getAmount)
                            .sum()
            );

            model.addAttribute("totalCustomers",
                    customerService.findAll().size());

            model.addAttribute("totalCars",
                    carService.findAll().size());

            model.addAttribute("totalBookings",
                    bookingService.findAll().size());

            model.addAttribute("totalPayments",
                    totalAmount);

            return "admin-home";
        }
        return "access-denied";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model){
        model.addAttribute("customer", new CustomerModel());
        model.addAttribute("roles", Role.getAllRoleNames());
        return "customer-form";
    }

    @PostMapping("/user-create")
    public String registerCustomer(Model model,
                                   @ModelAttribute CustomerModel customerModel){

        List<String> errors = customerValidator.validate(customerModel);
        if(!errors.isEmpty()) {
            model.addAttribute("error", errors);
            model.addAttribute("customer", new CustomerModel());
            model.addAttribute("roles", Role.getAllRoleNames());
            return "customer-form";
        }

        try {
            customerService.save(customerModel);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", new CustomerModel());
            model.addAttribute("roles", Role.getAllRoleNames());
            return "customer-form";
        }
        model.addAttribute("success", "Customer with username " + customerModel.getUsername() + " registered successfully");
        model.addAttribute("customer", new CustomerModel());
        return "login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout){
        if(error != null){
            model.addAttribute("error","Invalid username OR password");
        }
        if(logout != null){
            model.addAttribute("success", "You have been logged out successfully");
        }

        return "login";
    }
    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/profile/edit/{id}")
    public String editProfile(@PathVariable Long id,
                              Authentication authentication,
                              Model model) {

        try {

            if(authentication == null
                    || !authentication.isAuthenticated()) {

                return "redirect:/rentify/login";
            }

            CustomerModel customer =
                    customerService.findById(id);

            if(customer == null) {

                return "access-denied";
            }

            String loggedInUsername =
                    authentication.getName();

            // SECURITY CHECK

            if(customer.getUsername() == null
                    || !customer.getUsername()
                    .equals(loggedInUsername)) {

                return "access-denied";
            }

            model.addAttribute("customer", customer);

            model.addAttribute("roles",
                    Role.getAllRoleNames());

            return "customer-form";
        }

        catch (CustomerNotFoundException e) {

            return "access-denied";
        }
    }
}
