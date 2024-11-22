package com.muhammadminhaz.inventorymanagementsystem.dao;

import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface AdminDAO extends ListCrudRepository<Admin, Long> {
    Optional<Admin> findByUsernameAndPassword(String username, String password);
    Admin findByUsername(String username);
}
