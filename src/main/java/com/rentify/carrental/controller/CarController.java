package com.rentify.carrental.controller;

import com.rentify.carrental.exception.CarNotFoundException;
import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.CarModel;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.service.BookingService;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CompanyService;
import com.rentify.carrental.validators.CarValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rentify/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CarValidator validator;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String getCar(Model model){
        try{
            bookingService.autoUpdateBookingStatus();
            List<CarModel> cars = carService.findAll();
            if(cars.isEmpty()){
                model.addAttribute("error", "No car found");
            }else{
                model.addAttribute("success", cars.size()+" car found");
            }
            model.addAttribute("cars", cars);
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong while loading cars");
            model.addAttribute("cars", new ArrayList<>());
        }
        return "car";
    }
    
    @GetMapping("/new")
    public String addCar(Model model){
        model.addAttribute("car", new CarModel());
        model.addAttribute("companies", companyService.findAll());
        return "car-form";
    }

    @GetMapping("/edit/{id}")
    public String editById(@PathVariable Long id, Model model){
        try {
            CarModel car = carService.findById(id);
            model.addAttribute("car", car);
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("success", "Car found with id : "+id);
            return "car-form";
        } catch (CarNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cars", carService.findAll());
            return "car";
        }
    }

    @PostMapping("/save")
    public String saveCar(@ModelAttribute CarModel carModel, Model model){
        List<String> errors = validator.validate(carModel);
        if(!errors.isEmpty()){
            model.addAttribute("error", errors);
        }else{
            try {
                boolean isNew = (carModel.getId() == null);
                carService.save(carModel);
                if(isNew){
                    model.addAttribute("success", "Car added successfully");
                }else{
                    model.addAttribute("success", "Car updated successfully");
                }
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("cars", carService.findAll());
        return "car";
    }

    @DeleteMapping("/delete/{id}")
    public String removeCar(@PathVariable Long id, Model model){
        try {
            carService.removeById(id);
            model.addAttribute("success", "Car removed successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("cars", carService.findAll());
        return "car";
    }

    @GetMapping("/find/{id}")
    public String getCarById(@PathVariable Long id, Model model){
        try {
            CarModel car = carService.findById(id);
            model.addAttribute("success", "Car found");
            model.addAttribute("cars", List.of(car));
        } catch (CarNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cars", null);
        }
        return "car";
    }

}
