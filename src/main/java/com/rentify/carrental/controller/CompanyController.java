package com.rentify.carrental.controller;

import com.rentify.carrental.exception.CompanyNotFoundException;
import com.rentify.carrental.model.CompanyModel;
import com.rentify.carrental.service.CompanyService;
import com.rentify.carrental.validators.CompanyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rentify/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyValidator validator;

    @GetMapping("/")
    public String getCompany(Model model){
        List<CompanyModel> companies = companyService.findAll();
        if(companies.isEmpty()){
            model.addAttribute("error", "No company found");
        }else{
            model.addAttribute("success", companies.size()+" Company found");
        }
        model.addAttribute("companies", companies);
        return "company";
    }

    @GetMapping("/new")
    public String addCompany(Model model){
        model.addAttribute("company", new CompanyModel());
        return "company-form";
    }

    @PostMapping("/save")
    public String saveCompany(@ModelAttribute CompanyModel companyModel, Model model){
        List<String> errors = validator.validate(companyModel);
        if(!errors.isEmpty()){
            model.addAttribute("error", errors);
        }else{
            try {
                companyService.save(companyModel);
                if(companyModel.getId() == null){
                    model.addAttribute("success", "Company added successfully");
                }else{
                    model.addAttribute("success", "Company updated successfully");
                }
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("companies", companyService.findAll());
        return "company";
    }

    @GetMapping("/edit/{id}")
    public String updateCompany(@PathVariable Long id, Model model){
        try {
            CompanyModel company = companyService.findById(id);
            model.addAttribute("company", company);
            return "company-form";
        } catch (CompanyNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("companies", companyService.findAll());
        }
        return "company";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id, Model model){
        try {
            companyService.removeById(id);
            model.addAttribute("success", "Company removed successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("companies", companyService.findAll());
        return "company";
    }

    @GetMapping("/find/{id}")
    public String getCompanyById(@PathVariable Long id, Model model){
        try {
            CompanyModel company = companyService.findById(id);
            model.addAttribute("success", "Company found");
            model.addAttribute("companies", List.of(company));
        } catch (CompanyNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("companies", null);
        }
        return "company";
    }

}
