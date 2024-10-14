package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.model.Customer;
import com.muhammadminhaz.inventorymanagementsystem.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // Method to save a new customer
    public Customer save(Customer customer) {
        return customerDAO.save(customer);
    }

    public Customer findById(Long id) {
        Optional<Customer> customerOptional = customerDAO.findById(id);
        return customerOptional.orElse(null); // Return the customer or null if not found
    }
    // Method to find all customers
    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    // Method to update a customer
    public Customer update(Customer customer) {
        return customerDAO.save(customer);
    }

    // Method to delete a customer by ID
    public void deleteById(Long id) {
        customerDAO.deleteById(id);
    }
}

