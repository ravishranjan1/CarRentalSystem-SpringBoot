package com.rentify.carrental.repo;

import com.rentify.carrental.model.BookingModel;
import com.rentify.carrental.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<BookingModel, Long> {
    List<BookingModel> findByCustomer(CustomerModel customer);

    Optional<BookingModel> findByIdAndCustomer_Id(Long bookingId, Long customerId);

    @Query("""
            SELECT b FROM BookingModel b
            WHERE b.car.id = :carId
            AND b.id <> :bookingId
            AND b.status <> com.rentify.carrental.enums.CarStatus.CANCELLED
            AND b.status <> com.rentify.carrental.enums.CarStatus.RETURNED
            AND b.startDateTime < :newEnd
            AND b.endDateTime > :newStart
            """)
    List<BookingModel> findConflictingBookings(
            @Param("carId") Long carId,
            @Param("bookingId") Long bookingId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );
}
