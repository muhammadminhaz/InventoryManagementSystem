package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.dao.CustomerDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceItemDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.ProductDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceDAO invoiceDAO;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;
    private final InvoiceItemDAO invoiceItemDAO;
    private final AdminService adminService;

    @Autowired
    public InvoiceService(InvoiceDAO invoiceDAO, InvoiceItemDAO invoiceItemDAO, CustomerDAO customerDAO, ProductDAO productDAO, AdminService adminService) {
        this.invoiceDAO = invoiceDAO;
        this.invoiceItemDAO = invoiceItemDAO;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
        this.adminService = adminService;
    }

    public Invoice saveInvoice(Invoice invoice) {
        Admin currentAdmin = adminService.getCurrentAdmin();
        invoice.setAdmin(currentAdmin);
        return invoiceDAO.save(invoice);
    }

    public InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemDAO.save(invoiceItem);
    }

    public Optional<InvoiceItem> getInvoiceItemById(Long id) {
        return invoiceItemDAO.findById(id);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceDAO.findByAdmin(adminService.getCurrentAdmin());
    }

    public List<Invoice> getAllInvoicesByAdmin(Admin admin) {
        return invoiceDAO.findByAdmin(admin);
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceDAO.findById(id);
    }

    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(id);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            return invoiceDAO.save(invoice);
        }
        return null;
    }

    public void deleteInvoice(Long id) {
        List<InvoiceItem> invoiceItems = invoiceItemDAO.findInvoiceItemsByInvoiceId(id);
        for (InvoiceItem invoiceItem : invoiceItems) {
            invoiceItemDAO.deleteById(invoiceItem.getId());
        }
        invoiceDAO.deleteById(id);
    }

    public Invoice addProductToInvoice(Long invoiceId, Product product) {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            return invoiceDAO.save(invoice);
        }
        return null;
    }

    @Transactional
    public Invoice saveInvoiceWithItems(Long customerId, List<InvoiceItem> invoiceItems, Double discountAmount, LocalDateTime date) {
        Admin admin = adminService.getCurrentAdmin();
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Invoice invoice = new Invoice();
        LocalDateTime invoiceDate = date == null ? LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))) : date;
        invoice.setAdmin(admin);
        invoice.setCustomer(customer);
        invoice.setDate(invoiceDate);
        invoice.setTotalAmount(0.0);
        Double totalAmount = 0.0;
        List<Invoice> invoices = getAllInvoices();
        invoice.setAdminRefId((long) (invoices.size() + 1));
        for (InvoiceItem item : invoiceItems) {
            Product product = productDAO.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProduct(product);
            invoiceItem.setQuantity(item.getQuantity());
            if (item.getPrintedSide() != 0 && item.getPrintType() != "None") {
                invoiceItem.setCustomized(true);
            }
            invoiceItem.setPrintedSide(item.getPrintedSide());
            invoiceItem.setSerialNumber(item.getSerialNumber());
            invoiceItem.setPrintType(item.getPrintType());
            invoiceItem.setAdjustedPrice(item.getAdjustedPrice());
            product.setQuantity(product.getQuantity() - item.getQuantity());
            Double subTotal = item.getSubtotal();
            invoiceItem.setSubtotal(subTotal);
            totalAmount += subTotal;
            invoice.addInvoiceItem(invoiceItem);
        }

        if (discountAmount != null) {
            totalAmount -= discountAmount;
        }

        invoice.setTotalAmount(totalAmount);
        invoice.setDiscountAmount(discountAmount);
        return invoiceDAO.save(invoice);
    }

}
