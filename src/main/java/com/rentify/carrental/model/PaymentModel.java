package com.rentify.carrental.model;

import com.rentify.carrental.enums.PaymentMode;
import com.rentify.carrental.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment")
public class PaymentModel extends BaseModel{

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMode getMode() {
        return mode;
    }

    public void setMode(PaymentMode mode) {
        this.mode = mode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

}
