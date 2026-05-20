package com.rentify.carrental.controller;

import com.rentify.carrental.enums.CarStatus;
import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.service.BookingService;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CustomerService;
import com.rentify.carrental.validators.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rentify")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingValidator bookingValidator;

    @GetMapping("/admin/booking/")
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

    @GetMapping("/user/booking/new")
    public String bookingPage(Authentication authentication,
                              Model model) {

        String username =
                authentication.getName();

        CustomerModel customer =
                customerService.findByUsername(username);

        BookingModel booking =
                new BookingModel();

        // AUTO SET CUSTOMER

        booking.setCustomer(customer);

        model.addAttribute("booking", booking);

        model.addAttribute("cars",
                carService.findAll());

        return "booking-form";
    }

    @PostMapping("/common/booking/save")
    public String submitRentForm (@ModelAttribute BookingModel bookingModel, Model model){

        List<String> errors = bookingValidator.validate(bookingModel);
        if (!errors.isEmpty()) {
            model.addAttribute("error", errors);
            model.addAttribute("bookings", List.of());
            return "booking";
        }
        try{
            BookingModel booking = bookingService.booking(bookingModel);
            if(bookingModel.getId() == null){
                model.addAttribute("success", "Car is booked successfully");
                model.addAttribute("bookings", List.of(booking));
            }else{
                model.addAttribute("success", "Booking is updated successfully");
                model.addAttribute("bookings", List.of(booking));
            }
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", List.of());
        }
        return "booking";
    }

    @GetMapping("/common/booking/edit/{id}")
    public String openEditBookingForm(@PathVariable Long id, Model model) {

        BookingModel booking = null;
        try {
            booking = bookingService.findById(id);
            model.addAttribute("booking", booking);
            model.addAttribute("cars", carService.findAll());
            return "booking-edit-form";
        } catch (BookingNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings",bookingService.findAll());
            return "booking";
        }
    }


    @DeleteMapping("/common/booking/delete/{id}")
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

    @GetMapping("/common/booking/find/{id}")
    public String getBookingById(@PathVariable Long id, Model model){
        try {
            BookingModel booking = bookingService.findById(id);
            model.addAttribute("success", "Booking found");
            model.addAttribute("bookings", List.of(booking));
        } catch (BookingNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", List.of());
        }
        return "booking";
    }

    @GetMapping("/common/booking/cancel/{id}")
    public String cancelBooking(@PathVariable Long id,
                                Authentication authentication,
                                Model model){

        try{

            BookingModel booking =
                    bookingService.findById(id);

            String username =
                    authentication.getName();

            boolean isAdmin =
                    authentication.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                    a.getAuthority()
                                            .equals("ROLE_ADMIN"));

            // USER CAN CANCEL ONLY
            // HIS OWN BOOKING

            if(!isAdmin){

                CustomerModel loggedInCustomer =
                        customerService.findByUsername(username);

                BookingModel ownBooking =
                        bookingService.findByIdAndCustomer(
                                id,
                                loggedInCustomer.getId()
                        );

                if(ownBooking == null){

                    model.addAttribute(
                            "error",
                            "You cannot cancel another user's booking"
                    );

                    return "access-denied";
                }
            }

            // CANCEL BOOKING

            bookingService.cancelBooking(id);

            model.addAttribute(
                    "success",
                    "Booking has been cancelled successfully"
            );

            // ADMIN PAGE

            if(isAdmin){

                model.addAttribute(
                        "bookings",
                        bookingService.findAll()
                );

                return "booking";
            }

            // USER PAGE

            CustomerModel customer =
                    customerService.findByUsername(username);

            model.addAttribute(
                    "customer",
                    customer
            );

            model.addAttribute(
                    "bookingList",
                    bookingService.findByCustomer(customer)
            );

            model.addAttribute(
                    "carList",
                    carService.findAll()
            );

            return "user-home";

        }

        catch (Exception e){

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            return "access-denied";
        }
    }
}
