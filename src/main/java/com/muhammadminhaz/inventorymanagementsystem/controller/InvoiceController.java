package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.dao.CustomerDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceItemDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.ProductDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Customer;
import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import com.muhammadminhaz.inventorymanagementsystem.model.InvoiceItem;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import com.muhammadminhaz.inventorymanagementsystem.service.CustomerService;
import com.muhammadminhaz.inventorymanagementsystem.service.InvoiceService;
import com.muhammadminhaz.inventorymanagementsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomerDAO customerDAO;
    private ProductService productService;
    private CustomerService customerService;
    private InvoiceDAO invoiceDAO;
    private InvoiceItemDAO invoiceItemDAO;
    private ProductDAO productDAO;

    public InvoiceController(InvoiceService invoiceService, ProductService productService, CustomerService customerService, InvoiceDAO invoiceDAO, CustomerDAO customerDAO, InvoiceItemDAO invoiceItemDAO, ProductDAO productDAO) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.customerService = customerService;
        this.invoiceDAO = invoiceDAO;
        this.customerDAO = customerDAO;
        this.invoiceItemDAO = invoiceItemDAO;
    }

    // Method to show the list of invoices
    @GetMapping("/list")
    public String listInvoices(Model model) {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);
        return "invoice_list"; // Return the view name for invoice list
    }

    // Method to show the form for adding a new invoice
    @GetMapping("/add")
    public String showAddInvoiceForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("products", productService.getAllProducts());
        return "add_invoice"; // Return the view name for adding invoice
    }

    // Method to handle adding a new invoice
    @PostMapping("/create_invoice")
    public String createInvoice(@ModelAttribute Invoice invoice, Model model) {
        // Log the invoice details to see the selected products
        System.out.println("Invoice: " + invoice);

        Customer customer;
        if (customerDAO.findByNameAndEmail(invoice.getCustomer().getName(), invoice.getCustomer().getEmail()).isEmpty()) {
            customer = new Customer();
            customer.setName(invoice.getCustomer().getName());
            customer.setEmail(invoice.getCustomer().getEmail());
            customer.setPhone(invoice.getCustomer().getPhone());
            customer.setAddress(invoice.getCustomer().getAddress());
            customerService.save(customer);
        } else {
            customer = customerDAO.findByNameAndEmail(invoice.getCustomer().getName(), invoice.getCustomer().getEmail()).get();
        }
        invoice.setCustomer(customer);

        Invoice savedInvoice = invoiceService.saveInvoiceWithItems(customer.getId(), invoice.getInvoiceItems(), invoice.getDiscountAmount());

        model.addAttribute("invoice", invoice);
        return "redirect:/invoices/" + savedInvoice.getId() + "/details";
    }


    // Method to show the form for editing an existing invoice
    @GetMapping("/{id}/edit")
    public String showEditInvoiceForm(@PathVariable Long id, Model model) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            model.addAttribute("invoice", invoice);
            return "add_invoice"; // Return the view name for editing invoice
        }
        return "redirect:/invoices/list"; // If invoice not found, redirect to the list
    }

    // Method to handle updating an existing invoice
    @PutMapping("/{id}")
    public String updateInvoice(@PathVariable Long id, @ModelAttribute Invoice invoice) {
        invoiceService.updateInvoice(id, invoice);
        return "redirect:/invoices/list"; // Redirect to the invoice list
    }

    // Method to handle deleting an invoice
    @DeleteMapping("/{id}/delete")
    public String deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/invoices/list"; // Redirect to the invoice list
    }

    // Method to view details of a specific invoice
    @GetMapping("/{id}/details")
    public String viewInvoiceDetails(@PathVariable Long id, Model model) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy hh:mm:ss a");
            String formattedDate = invoice.get().getDate().format(formatter);

            model.addAttribute("invoice", invoice.get());
            model.addAttribute("formattedDate", formattedDate);
            return "invoice_details"; // Return the view name for viewing invoice details
        }
        return "redirect:/invoices/list"; // If invoice not found, redirect to the list
    }

}
