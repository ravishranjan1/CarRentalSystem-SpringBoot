package com.rentify.carrental.service.impl;

import com.rentify.carrental.enums.CarStatus;
import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.exception.CarNotAvailableException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CarModel;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.repo.BookingRepo;
import com.rentify.carrental.service.BookingService;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarService carService;

    @Override
    public void rentCar(BookingModel bookingModel) throws Exception {
        try{
            CustomerModel customer = customerService.findById(bookingModel.getCustomer().getId());
            CarModel car = carService.findById((bookingModel.getCar().getId()));
            if(car.getAvailable()== true){
                bookingModel.setCustomer(customer);
                bookingModel.setCar(car);
                bookingModel.setStatus(CarStatus.ONGOING);
                bookingRepo.save(bookingModel);
                car.setAvailable(false);
                carService.save(car);
            }else{
                throw new CarNotAvailableException("Car Not Available for now, Choose another car");
            }
        }catch(Exception e){
            throw new Exception("error while renting car");
        }
    }

    @Override
    public BookingModel returnCar(BookingModel booking) throws Exception {

        booking.setStatus(CarStatus.RETURNED);

        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;
        double totalPrice = days * booking.getCar().getPricePerday();
        booking.setTotalAmount(totalPrice);

        BookingModel bookingModel = bookingRepo.save(booking);

        CarModel car = booking.getCar();
        car.setAvailable(true);
        carService.save(car);
        return booking;
    }


    @Override
    public BookingModel findById(Long id) throws BookingNotFoundException {
        Optional<BookingModel> opt = bookingRepo.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new BookingNotFoundException("Booking not found with id : "+id);
        }
    }

    @Override
    public void removeById(Long id) throws Exception {
        Optional<BookingModel> opt = bookingRepo.findById(id);
        if(opt.isPresent()){
            BookingModel booking = opt.get();
            if(booking.getStatus() == CarStatus.RETURNED){
                bookingRepo.delete(booking);
            }else{
                throw new Exception("Car is ONGOING, you cannot remove it now!");
            }
        }else{
            throw new BookingNotFoundException("Booking not found with id : "+id);
        }
    }

    @Override
    public List<BookingModel> findAll() {
        return bookingRepo.findAll();
    }


}
