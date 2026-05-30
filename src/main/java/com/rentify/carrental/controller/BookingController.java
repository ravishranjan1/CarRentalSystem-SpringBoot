package com.rentify.carrental.controller;

import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.exception.CarNotFoundException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.service.BookingService;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CustomerService;
import com.rentify.carrental.service.PdfService;
import com.rentify.carrental.validators.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;

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

    @Autowired
    private PdfService pdfService;

    @GetMapping("/admin/booking/")
    public String getBooking(Model model) {
        try {
            List<BookingModel> bookings = bookingService.findAll();
            if (bookings.isEmpty()) {
                model.addAttribute("error", "No booking found");
            } else {
                model.addAttribute("success", bookings.size() + " Booking found");
            }
            model.addAttribute("bookings", bookings);
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong while loading cars");
            model.addAttribute("cars", new ArrayList<>());
        }
        return "booking";
    }

    @GetMapping("/user/booking/new")
    public String bookingPage(
            @RequestParam(required = false) Long carId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Authentication authentication,
            Model model) {
        String username = authentication.getName();
        CustomerModel customer = customerService.findByUsername(username);
        BookingModel booking = new BookingModel();
        booking.setCustomer(customer);
        if (carId != null) {
            try {
                booking.setCar(carService.findById(carId));
            } catch (CarNotFoundException e) {
                model.addAttribute("error", "Selected car not found");
                model.addAttribute("booking", booking);
                model.addAttribute("cars", carService.findAll());
                return "booking-form";
            }
        }
        if (startDate != null && !startDate.isEmpty()) {
            booking.setStartDateTime(java.time.LocalDateTime.parse(startDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            booking.setEndDateTime(java.time.LocalDateTime.parse(endDate));
        }
        model.addAttribute("booking", booking);
        return "booking-form";
    }

    @PostMapping("/admin/booking/save")
    public String adminSubmitRentForm(@ModelAttribute BookingModel bookingModel, Model model) {
        List<String> errors = bookingValidator.validate(bookingModel);
        if (!errors.isEmpty()) {
            model.addAttribute("error", errors);
            model.addAttribute("bookings", List.of());
            return "booking";
        }
        try {
            BookingModel booking = bookingService.booking(bookingModel);
            if (bookingModel.getId() == null) {
                model.addAttribute("success", "Car is booked successfully");
                model.addAttribute("bookings", booking);
            } else {
                model.addAttribute("success", "Booking is updated successfully");
                model.addAttribute("bookings", booking);
            }
            return "booking-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", List.of());
        }
        return "booking";
    }

    @PostMapping("/user/booking/save")
    public String userSubmitRentForm(@ModelAttribute BookingModel bookingModel, Model model) {
        List<String> errors = bookingValidator.validate(bookingModel);
        if (!errors.isEmpty()) {
            model.addAttribute("error", errors);
            model.addAttribute("bookings", List.of());
            return "booking";
        }
        try {
            BookingModel booking = bookingService.booking(bookingModel);
            if (bookingModel.getId() == null) {
                model.addAttribute("success", "Car is booked successfully");
                model.addAttribute("bookings", booking);
            } else {
                model.addAttribute("success", "Booking is updated successfully");
                model.addAttribute("bookings", booking);
            }
            return "booking-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", List.of());
        }
        return "booking";
    }

    @GetMapping("/admin/booking/edit/{id}")
    public String adminOpenEditBookingForm(@PathVariable Long id, Model model) {

        BookingModel booking = null;
        try {
            booking = bookingService.findById(id);
            model.addAttribute("booking", booking);
            model.addAttribute("cars", carService.findAll());
            return "booking-edit-form";
        } catch (BookingNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", bookingService.findAll());
            return "booking";
        }
    }

    @GetMapping("/user/booking/edit/{id}")
    public String userOpenEditBookingForm(@PathVariable Long id, Model model) {

        BookingModel booking = null;
        try {
            booking = bookingService.findById(id);
            model.addAttribute("booking", booking);
            model.addAttribute("cars", carService.findAll());
            return "booking-edit-form";
        } catch (BookingNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookings", bookingService.findAll());
            return "booking";
        }
    }


    @DeleteMapping("/admin/booking/delete/{id}")
    public String adminDeleteBooking(@PathVariable Long id, Model model) {
        try {
            bookingService.removeById(id);
            model.addAttribute("success", "booking removed successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("bookings", bookingService.findAll());
        return "booking";
    }

    @DeleteMapping("/user/booking/delete/{id}")
    public String userDeleteBooking(@PathVariable Long id, Model model) {
        try {
            bookingService.removeById(id);
            model.addAttribute("success", "booking removed successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("bookings", bookingService.findAll());
        return "booking";
    }

    @GetMapping("/admin/booking/find/{id}")
    public String adminGetBookingById(@PathVariable Long id, Model model) {
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

    @GetMapping("/user/booking/find/{id}")
    public String userGetBookingById(@PathVariable Long id, Model model) {
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

    @GetMapping("/admin/booking/cancel/{id}")
    public String adminCancelBooking(@PathVariable Long id, Authentication authentication, Model model) {
        try {
            BookingModel booking = bookingService.findById(id);
            String username = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                CustomerModel loggedInCustomer = customerService.findByUsername(username);
                BookingModel ownBooking = bookingService.findByIdAndCustomer(id, loggedInCustomer.getId());
                if (ownBooking == null) {
                    model.addAttribute("error", "You cannot cancel another user's booking");
                    return "access-denied";
                }
            }
            bookingService.cancelBooking(id);
            model.addAttribute("success", "Booking has been cancelled successfully");
            if (isAdmin) {
                model.addAttribute("bookings", bookingService.findAll());
                return "booking";
            }
            CustomerModel customer = customerService.findByUsername(username);
            model.addAttribute("customer", customer);
            model.addAttribute("bookingList", bookingService.findByCustomer(customer));
            model.addAttribute("carList", carService.findAll());
            return "user-home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/user/booking/cancel/{id}")
    public String userCancelBooking(@PathVariable Long id, Authentication authentication, Model model) {
        try {
            BookingModel booking = bookingService.findById(id);
            String username = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                CustomerModel loggedInCustomer = customerService.findByUsername(username);
                BookingModel ownBooking = bookingService.findByIdAndCustomer(id, loggedInCustomer.getId());
                if (ownBooking == null) {
                    model.addAttribute("error", "You cannot cancel another user's booking");
                    return "access-denied";
                }
            }
            bookingService.cancelBooking(id);
            model.addAttribute("success", "Booking has been cancelled successfully");
            if (isAdmin) {
                model.addAttribute("bookings", bookingService.findAll());
                return "booking";
            }
            CustomerModel customer = customerService.findByUsername(username);
            model.addAttribute("customer", customer);
            model.addAttribute("bookingList", bookingService.findByCustomer(customer));
            model.addAttribute("carList", carService.findAll());
            return "user-home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/admin/booking/pdf/{id}")
    public ResponseEntity<InputStreamResource> adminDownloadBookingPdf(@PathVariable Long id) throws Exception {
        BookingModel booking = bookingService.findById(id);
        ByteArrayInputStream pdf = pdfService.generateBookingPdf(booking);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=booking-receipt.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    @GetMapping("/user/booking/pdf/{id}")
    public ResponseEntity<InputStreamResource> userDownloadBookingPdf(@PathVariable Long id) throws Exception {
        BookingModel booking = bookingService.findById(id);
        ByteArrayInputStream pdf = pdfService.generateBookingPdf(booking);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=booking-receipt.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }
}
