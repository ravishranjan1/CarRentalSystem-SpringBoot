package com.rentify.carrental.model;

import com.rentify.carrental.enums.CarStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class BookingModel extends BaseModel{


    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @OneToOne
    private PaymentModel payment;

    @ManyToOne
    private CustomerModel customer;

    @ManyToOne
    private CarModel car;

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }

    public CarModel getCar() {
        return car;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public PaymentModel getPayment() {
        return payment;
    }

    public void setPayment(PaymentModel payment) {
        this.payment = payment;
    }
}
