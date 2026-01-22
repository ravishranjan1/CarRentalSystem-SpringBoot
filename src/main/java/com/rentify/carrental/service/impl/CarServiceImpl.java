package com.rentify.carrental.service.impl;

import com.rentify.carrental.exception.CarNotFoundException;
import com.rentify.carrental.exception.CompanyNotFoundException;
import com.rentify.carrental.model.CarModel;
import com.rentify.carrental.model.CompanyModel;
import com.rentify.carrental.repo.CarRepo;
import com.rentify.carrental.service.CarService;
import com.rentify.carrental.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private CompanyService companyService;

    @Override
    public void add(CarModel carModel) throws Exception {
        try{
            CompanyModel company = companyService.findById(carModel.getCompany().getId());
            carModel.setCompany(company);
            carModel.setAvailable(true);
            carRepo.save(carModel);

        }catch(CompanyNotFoundException e){
            throw new CompanyNotFoundException("Company not found with this name");
        } catch(Exception e){
            throw new Exception("Error while adding the car");
        }
    }

    @Override
    public void update(CarModel carModel) throws CarNotFoundException, CompanyNotFoundException {
        Optional<CarModel> opt = carRepo.findById(carModel.getId());
        if(opt.isPresent()){
            CarModel updateCar = opt.get();
            CompanyModel company = companyService.findById(carModel.getCompany().getId());
            updateCar.setCompany(company);
            updateCar.setModel(carModel.getModel());
            updateCar.setRegistrationNo(carModel.getRegistrationNo());
            updateCar.setType(carModel.getType());
            updateCar.setPricePerday(carModel.getPricePerday());
            updateCar.setAvailable(carModel.getAvailable());
            carRepo.save(updateCar);
        }else{
            throw new CarNotFoundException("Car not found with id : "+carModel.getId());
        }
    }

    @Override
    public void removeById(Long id) throws Exception {
        try{
            carRepo.deleteById(id);
        }catch(Exception e){
            throw new Exception("Error while removing car");
        }
    }

    @Override
    public List<CarModel> findAll() {
        return carRepo.findAll();
    }

    @Override
    public CarModel findById(Long id) throws CarNotFoundException {
        Optional<CarModel> opt = carRepo.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new CarNotFoundException("Car not found with id : "+id);
        }
    }
}
