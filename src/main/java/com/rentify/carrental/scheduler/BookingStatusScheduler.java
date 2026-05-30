package com.rentify.carrental.scheduler;

import com.rentify.carrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusScheduler {

    @Autowired
    private BookingService bookingService;

    @Scheduled(fixedRate = 60000)
    public void autoUpdateBookingStatus(){
        try {
            bookingService.autoUpdateBookingStatus();
            System.out.println("Booking statuses updated automatically");
        } catch (Exception e) {
            System.err.println("Error updating booking statuses");
            e.printStackTrace();
        }
    }
}
