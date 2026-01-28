package com.rentify.carrental.controller;

import com.rentify.carrental.enums.CarStatus;
import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.service.BookingService;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CustomerService;
import com.rentify.carrental.validators.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rentify/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingValidator bookingValidator;

    @GetMapping("/")
    public String getBooking(Model model){
        try{
            bookingService.autoUpdateBookingStatus();
            List<BookingModel> bookings = bookingService.findAll();
            if(bookings.isEmpty()){
                model.addAttribute("error", "No booking found");
            }else{
                model.addAttribute("success", bookings.size()+" Booking found");
            }
            model.addAttribute("bookings", bookings);
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong while loading cars");
            model.addAttribute("cars", new ArrayList<>());
        }
        return "booking";
    }

    @GetMapping("/new")
    public String openBookingForm(Model model){
        model.addAttribute("booking", new BookingModel());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("cars", carService.findAll());
        return "booking-form";
    }

    @PostMapping("/save")
    public String submitRentForm (@ModelAttribute BookingModel bookingModel, Model model){

        List<String> errors = bookingValidator.validate(bookingModel);
        if (!errors.isEmpty()) {
            model.addAttribute("error", errors);
            model.addAttribute("bookings", bookingService.findAll());
            return "booking";
        }
        try{
            if(bookingModel.getId() == null){
                bookingService.booking(bookingModel);
                model.addAttribute("success", "Car is booked successfully");
                model.addAttribute("bookings", List.of(bookingService.findById(bookingModel.getId())));
            }else{
                bookingService.booking(bookingModel);
                model.addAttribute("success", "Booking is updated successfully");
                model.addAttribute("bookings", List.of(bookingService.findById(bookingModel.getId())));
            }
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", null);
        }
        return "booking";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id, Model model){
        try {
            bookingService.removeById(id);
            model.addAttribute("success", "booking removed successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("bookings", bookingService.findAll());
        return "booking";
    }

    @GetMapping("/find/{id}")
    public String getBookingById(@PathVariable Long id, Model model){
        try {
            BookingModel booking = bookingService.findById(id);
            model.addAttribute("success", "Booking found");
            model.addAttribute("bookings", List.of(booking));
        } catch (BookingNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", null);
        }
        return "booking";
    }
}
