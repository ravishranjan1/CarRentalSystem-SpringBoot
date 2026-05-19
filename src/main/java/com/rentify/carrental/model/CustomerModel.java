package com.rentify.carrental.model;

import com.rentify.carrental.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerModel extends BaseModel{

    private String name;
    private Long phone;
    @Column(unique = true)
    private String drivingLicenseNo;

    @Column(nullable = false)
    private Role role;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;

    public CustomerModel(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public CustomerModel(String username, String password){
        this.username = username;
        this.password = password;
    }
    public CustomerModel(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getDrivingLicenseNo() {
        return drivingLicenseNo;
    }

    public void setDrivingLicenseNo(String drivingLicenseNo) {
        this.drivingLicenseNo = drivingLicenseNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
