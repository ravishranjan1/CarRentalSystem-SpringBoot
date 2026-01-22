package com.rentify.carrental.service;

import com.rentify.carrental.exception.CompanyNotFoundException;
import com.rentify.carrental.exception.CustomerNotFoundException;
import com.rentify.carrental.model.CompanyModel;
import com.rentify.carrental.model.CustomerModel;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    void save(CompanyModel companyModel) throws Exception;

    List<CompanyModel> findAll();
    CompanyModel findById(Long id) throws CompanyNotFoundException;

    void removeById(Long id) throws Exception;
}
