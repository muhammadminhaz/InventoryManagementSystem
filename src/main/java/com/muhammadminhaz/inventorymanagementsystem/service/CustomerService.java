package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Customer;
import com.muhammadminhaz.inventorymanagementsystem.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;
    private final AdminService adminService;

    @Autowired
    public CustomerService(CustomerDAO customerDAO, AdminService adminService) {
        this.customerDAO = customerDAO;
        this.adminService = adminService;
    }

    public Customer save(Customer customer) {
        Admin currentAdmin = adminService.getCurrentAdmin();
        customer.setAdmin(currentAdmin);
        return customerDAO.save(customer);
    }

    public Customer findById(Long id) {
        Optional<Customer> customerOptional = customerDAO.findById(id);
        return customerOptional.orElse(null);
    }

    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    public Customer update(Customer customer) {
        return customerDAO.save(customer);
    }

    public void deleteById(Long id) {
        customerDAO.deleteById(id);
    }
}

