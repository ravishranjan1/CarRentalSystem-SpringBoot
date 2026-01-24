package com.rentify.carrental.service.impl;

import com.rentify.carrental.exception.CompanyNotFoundException;
import com.rentify.carrental.model.CompanyModel;
import com.rentify.carrental.repo.CompanyRepo;
import com.rentify.carrental.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepo companyRepo;


    @Override
    public void save(CompanyModel companyModel) throws Exception {
        try{
            companyModel.setName(companyModel.getName().toUpperCase());
            companyModel.setCountry(companyModel.getCountry().toUpperCase());

            if(companyModel.getId() == null){
                companyRepo.save(companyModel);
            }else{
                Optional<CompanyModel> opt = companyRepo.findById(companyModel.getId());
                if(opt.isPresent()){
                    CompanyModel updateCompany = opt.get();
                    updateCompany.setName(companyModel.getName());
                    updateCompany.setCountry(companyModel.getCountry());
                    companyRepo.save(updateCompany);
                }else{
                    throw new CompanyNotFoundException("Company not found with given id : "+companyModel.getId());
                }
            }
        } catch(CompanyNotFoundException e){
            throw e;
        } catch(Exception e){
            throw new Exception("Error while adding or updating Company ");
        }
    }

    @Override
    public List<CompanyModel> findAll() {
        return companyRepo.findAll();
    }

    @Override
    public CompanyModel findById(Long id) throws CompanyNotFoundException {
        Optional<CompanyModel> opt = companyRepo.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new CompanyNotFoundException("Company not found by id : "+id);
        }

    }

    @Override
    public void removeById(Long id) throws Exception {
        try{
            companyRepo.deleteById(id);
        }catch(Exception e){
            throw new Exception("Error while deleting company");
        }
    }
}
