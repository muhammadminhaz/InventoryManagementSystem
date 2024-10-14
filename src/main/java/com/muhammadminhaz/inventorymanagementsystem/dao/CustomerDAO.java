package com.muhammadminhaz.inventorymanagementsystem.dao;

import com.muhammadminhaz.inventorymanagementsystem.model.Customer;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CustomerDAO extends ListCrudRepository<Customer, Long> {
    Optional<Customer> findByNameAndEmail(String name, String email);
}
