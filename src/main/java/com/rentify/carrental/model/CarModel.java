package com.rentify.carrental.model;

import com.rentify.carrental.enums.CarType;
import jakarta.persistence.*;

@Entity
@Table(name = "car")
public class CarModel extends BaseModel{

    private String model;
    private String registrationNo;

    @Enumerated(EnumType.STRING)
    private CarType type;

    private double pricePerday;

    @ManyToOne
    private CompanyModel company;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    public double getPricePerday() {
        return pricePerday;
    }

    public void setPricePerday(double pricePerday) {
        this.pricePerday = pricePerday;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }
}
