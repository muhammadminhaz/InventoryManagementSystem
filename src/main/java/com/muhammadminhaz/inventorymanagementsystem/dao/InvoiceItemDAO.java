package com.muhammadminhaz.inventorymanagementsystem.dao;

import com.muhammadminhaz.inventorymanagementsystem.model.InvoiceItem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InvoiceItemDAO extends ListCrudRepository<InvoiceItem, Long> {
    List<InvoiceItem> findInvoiceItemsByInvoiceId(Long invoiceId);
}
