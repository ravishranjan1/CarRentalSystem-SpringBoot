package com.rentify.carrental.service.impl;

import com.rentify.carrental.enums.CarStatus;
import com.rentify.carrental.enums.PaymentStatus;
import com.rentify.carrental.exception.BookingNotFoundException;
import com.rentify.carrental.exception.CarNotAvailableException;
import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CarModel;
import com.rentify.carrental.model.CustomerModel;
import com.rentify.carrental.model.PaymentModel;
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
    public void booking(BookingModel bookingModel) throws Exception {

        try {
            if(bookingModel.getId()==null){
                CustomerModel customer = customerService.findById(bookingModel.getCustomer().getId());
                CarModel car = carService.findById(bookingModel.getCar().getId());

                bookingModel.setCustomer(customer);
                bookingModel.setCar(car);
                bookingModel.setStatus(CarStatus.SCHEDULED);

                long days = ChronoUnit.DAYS.between(bookingModel.getStartDate(),
                        bookingModel.getEndDate()) + 1;

                double totalPrice = days * car.getPricePerday();
                bookingModel.setTotalAmount(totalPrice);

                PaymentModel payment = new PaymentModel();
                payment.setAmount(totalPrice);
                payment.setMode(bookingModel.getPayment().getMode());
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setBookingModel(bookingModel);
                bookingModel.setPayment(payment);

                bookingRepo.save(bookingModel);
                car.setAvailable(false);
                carService.save(car);
            }else{
                Optional<BookingModel> opt = bookingRepo.findById(bookingModel.getId());
                if(opt.isEmpty()){
                    throw new Exception("Booking not found");
                }
                BookingModel booking = opt.get();
                if (booking.getStatus() != CarStatus.SCHEDULED) {
                    throw new Exception("Booking cannot be edited after rent");
                }
                booking.setStatus(bookingModel.getStatus());
                booking.setPayment(bookingModel.getPayment());
                booking.setStartDate(bookingModel.getStartDate());
                booking.setEndDate(bookingModel.getEndDate());
                booking.setCar(bookingModel.getCar());
                booking.setCustomer(bookingModel.getCustomer());
                booking.setTotalAmount(bookingModel.getTotalAmount());
                bookingRepo.save(booking);
            }
        } catch (Exception e) {
            throw new Exception("Error while creating booking"+e.getMessage());
        }
    }


    public void autoUpdateBookingStatus() throws Exception{
        try{
            LocalDate today = LocalDate.now();
            List<BookingModel> bookings = bookingRepo.findAll();
            for (BookingModel booking : bookings) {
                if (booking.getStatus() == CarStatus.SCHEDULED && booking.getStartDate() != null && booking.getStartDate().equals(today)) {
                    booking.setStatus(CarStatus.ONGOING);
                    if (booking.getCar() != null) {
                        CarModel car = booking.getCar();
                        car.setAvailable(false);
                        carService.save(car);
                    }
                }
                if (booking.getStatus() == CarStatus.ONGOING && booking.getEndDate() != null && booking.getEndDate().isBefore(today)) {
                    booking.setStatus(CarStatus.RETURNED);
                    if (booking.getCar() != null) {
                        CarModel car = booking.getCar();
                        car.setAvailable(true);
                        carService.save(car);
                    }
                }
            }
            bookingRepo.saveAll(bookings);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
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
                throw new Exception("Car is ONGOING or SCHEDULED, you cannot remove it now!");
            }
        }else{
            throw new BookingNotFoundException("Booking not found with id : "+id);
        }
    }

    @Override
    public List<BookingModel> findAll() {
        return bookingRepo.findAll();
    }

    @Override
    public boolean isCarAvailable(Long carId, LocalDate from, LocalDate to) {

        List<BookingModel> bookings = bookingRepo.findAll();

        for (BookingModel booking : bookings) {

            if (booking.getCar() != null
                    && booking.getCar().getId().equals(carId)
                    && booking.getStatus() != CarStatus.RETURNED) {

                LocalDate existingFrom = booking.getStartDate();
                LocalDate existingTo = booking.getEndDate();

                if (from.isBefore(existingTo) && to.isAfter(existingFrom)) {
                    return false;
                }
            }
        }
        return true;
    }

}
