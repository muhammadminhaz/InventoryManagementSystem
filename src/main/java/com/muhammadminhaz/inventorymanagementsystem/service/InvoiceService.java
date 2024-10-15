package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.dao.CustomerDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceItemDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.ProductDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Customer;
import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import com.muhammadminhaz.inventorymanagementsystem.model.InvoiceItem;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceDAO invoiceDAO;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;
    private InvoiceItemDAO invoiceItemDAO;

    @Autowired
    public InvoiceService(InvoiceDAO invoiceDAO, InvoiceItemDAO invoiceItemDAO, CustomerDAO customerDAO, ProductDAO productDAO) {
        this.invoiceDAO = invoiceDAO;
        this.invoiceItemDAO = invoiceItemDAO;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    // Create a new invoice
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceDAO.save(invoice);
    }

    public InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemDAO.save(invoiceItem);
    }

    public Optional<InvoiceItem> getInvoiceItemById(Long id) {
        return invoiceItemDAO.findById(id);
    }

    // Retrieve all invoices
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.findAll();
    }

    // Retrieve a specific invoice by ID
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceDAO.findById(id);
    }

    // Update an existing invoice
    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(id);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            // Update other fields as necessary (e.g., invoice date)
            return invoiceDAO.save(invoice);
        }
        return null; // or throw an exception
    }

    // Delete an invoice
    public void deleteInvoice(Long id) {
        List<InvoiceItem> invoiceItems = invoiceItemDAO.findInvoiceItemsByInvoiceId(id);
        for (InvoiceItem invoiceItem : invoiceItems) {
            invoiceItemDAO.deleteById(invoiceItem.getId());
        }
        invoiceDAO.deleteById(id);
    }

    // Add a product to an invoice
    public Invoice addProductToInvoice(Long invoiceId, Product product) {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            return invoiceDAO.save(invoice); // Save the updated invoice
        }
        return null; // or throw an exception
    }

    // Remove a product from an invoice
    public Invoice removeProductFromInvoice(Long invoiceId, Long productId) {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
//            Product productToRemove = invoice.getProducts()
//                    .stream()
//                    .filter(product -> product.getId().equals(productId))
//                    .findFirst()
//                    .orElse(null);
//            if (productToRemove != null) {
//                invoice.removeProduct(productToRemove); // Remove the product from the invoice
//                return invoiceDAO.save(invoice); // Save the updated invoice
//            }
        }
        return null; // or throw an exception
    }


    @Transactional
    public Invoice saveInvoiceWithItems(Long customerId, List<InvoiceItem> invoiceItems, Double discountAmount) {
        // Fetch the existing customer, or create a new one
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create a new Invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setDate(LocalDateTime.now()); // Set current date, or any other logic
        invoice.setTotalAmount(0.0); // Initialize to zero, will update later

        for (InvoiceItem item : invoiceItems) {
            Product product = productDAO.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProduct(product);
            invoiceItem.setQuantity(item.getQuantity());
            product.setQuantity(product.getQuantity() - item.getQuantity());
            invoiceItem.setSubtotal((product.getPrice() * item.getQuantity()));

            invoice.addInvoiceItem(invoiceItem);
        }

        // Calculate total amount
        Double totalAmount = 0.0;
        for (InvoiceItem invoiceItem : invoiceItems) {
            totalAmount += invoiceItem.getSubtotal();
        }
        if (discountAmount != null) {
            totalAmount -= discountAmount;
        }

        invoice.setTotalAmount(totalAmount);
        invoice.setDiscountAmount(discountAmount);
        return invoiceDAO.save(invoice);
    }

}
