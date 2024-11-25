package com.muhammadminhaz.inventorymanagementsystem.dao;

import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
    List<Product> findAllByAdmin(Admin admin);
}
