package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final InvoiceService invoiceService;

    public DashboardService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public Map<String, Object> salesData(Admin admin) {
        List<Invoice> invoices = invoiceService.getAllInvoicesByAdmin(admin);

        invoices.sort(Comparator.comparing(Invoice::getDate));
        List<Double> sales = invoices.stream()
                .map(Invoice::getTotalAmount)
                .toList();
        List<Date> dates = invoices.stream()
                .map(invoice -> convertToDate(invoice.getDate()))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("sales", sales);
        result.put("dates", dates);

        return result;
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
