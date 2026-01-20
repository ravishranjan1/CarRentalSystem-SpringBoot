package com.rentify.carrental.repo;

import com.rentify.carrental.model.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyModel, Long> {
}
