package com.muhammadminhaz.inventorymanagementsystem.dao;

import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InvoiceDAO extends ListCrudRepository<Invoice, Long> {
    List<Invoice> findByAdmin(Admin admin);
}
