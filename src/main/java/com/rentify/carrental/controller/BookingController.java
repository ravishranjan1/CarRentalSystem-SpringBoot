package com.rentify.carrental.controller;

import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.exception.CarNotFoundException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CarModel;
import com.rentify.carrental.model.PaymentModel;
import com.rentify.carrental.service.BookingService;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CustomerService;
import com.rentify.carrental.validators.RentCarValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private RentCarValidator rentCarValidator;

    @Autowired
    private RentCarValidator returnCarValidator;

    @GetMapping("/")
    public String getBooking(Model model){
        List<BookingModel> bookings = bookingService.findAll();

        if(bookings.isEmpty()){
            model.addAttribute("error", "No booking found");
        }else{
            model.addAttribute("success", bookings.size()+" Booking found");
        }
        model.addAttribute("bookings", bookings);
        return "booking";
    }

    @GetMapping("/rent")
    public String openRentForm(Model model){
        model.addAttribute("booking", new BookingModel());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("cars", carService.findAll());
        return "rent-form";
    }

    @PostMapping("/rent")
    public String submitRentForm (@ModelAttribute BookingModel bookingModel, Model model){
        try {
            List<String> errors = rentCarValidator.validate(bookingModel);
            if (!errors.isEmpty()) {
                model.addAttribute("error", errors);
                model.addAttribute("bookings", bookingService.findAll());
                return "booking";
            }
            bookingService.rentCar(bookingModel);
            model.addAttribute("success", "Car is rented successfully");
            model.addAttribute("bookings", List.of(bookingService.findById(bookingModel.getId())));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", null);
        }
        return "booking";
    }

    @GetMapping("/return")
    public String openReturnForm(Model model){
        model.addAttribute("bookings", bookingService.findAll());
        return "return-form";
    }

    @PostMapping("/return")
    public String submitReturnForm(@RequestParam Long id, @RequestParam LocalDate returnDate, Model model ){
        try {
            BookingModel bookingModel = bookingService.findById(id);

            bookingModel.setEndDate(returnDate);

            List<String> errors = returnCarValidator.validate(bookingModel);
            if (!errors.isEmpty()) {
                model.addAttribute("error", errors);
                model.addAttribute("bookings", bookingService.findAll());
                return "booking";
            }

            BookingModel booking = bookingService.returnCar(bookingModel);
            model.addAttribute("booking", booking);
            model.addAttribute("success", "Car returned successfully");
            PaymentModel paymentModel = new PaymentModel();
            paymentModel.setBookingModel(booking);
            paymentModel.setAmount(booking.getTotalAmount());
            model.addAttribute("payment", paymentModel);
            return "payment-form";
        } catch (Exception e) {
            model.addAttribute("bookings", bookingService.findAll());
            model.addAttribute("error", e.getMessage());
            return "booking";
        }
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
