package com.muhammadminhaz.inventorymanagementsystem.dao;

import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import org.springframework.data.repository.ListCrudRepository;

public interface InvoiceDAO extends ListCrudRepository<Invoice, Long> {

}
