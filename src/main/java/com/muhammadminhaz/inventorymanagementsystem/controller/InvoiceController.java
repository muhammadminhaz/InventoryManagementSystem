package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.dao.CustomerDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.InvoiceItemDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.ProductDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Customer;
import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import com.muhammadminhaz.inventorymanagementsystem.model.InvoiceItem;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import com.muhammadminhaz.inventorymanagementsystem.service.AdminService;
import com.muhammadminhaz.inventorymanagementsystem.service.CustomerService;
import com.muhammadminhaz.inventorymanagementsystem.service.InvoiceService;
import com.muhammadminhaz.inventorymanagementsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomerDAO customerDAO;
    private final AdminService adminService;
    private ProductService productService;
    private CustomerService customerService;
    private InvoiceDAO invoiceDAO;
    private InvoiceItemDAO invoiceItemDAO;
    private ProductDAO productDAO;

    public InvoiceController(InvoiceService invoiceService, ProductService productService, CustomerService customerService, InvoiceDAO invoiceDAO, CustomerDAO customerDAO, InvoiceItemDAO invoiceItemDAO, ProductDAO productDAO, AdminService adminService) {
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.customerService = customerService;
        this.invoiceDAO = invoiceDAO;
        this.customerDAO = customerDAO;
        this.invoiceItemDAO = invoiceItemDAO;
        this.adminService = adminService;
    }

    @GetMapping("/list")
    public String listInvoices(Model model) {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);
        return "invoice_list";
    }

    @GetMapping("/add")
    public String showAddInvoiceForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("products", productService.getAllProducts());
        return "add_invoice";
    }

    @PostMapping("/create_invoice")
    public String createInvoice(@ModelAttribute Invoice invoice, Model model) {
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

        Invoice savedInvoice = invoiceService.saveInvoiceWithItems(customer.getId(), invoice.getInvoiceItems(), invoice.getDiscountAmount(), invoice.getDate());

        model.addAttribute("invoice", invoice);
        return "redirect:/invoices/" + savedInvoice.getId() + "/details";
    }


    @GetMapping("/{id}/edit")
    public String showEditInvoiceForm(@PathVariable Long id, Model model) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            model.addAttribute("invoice", invoice);
            return "add_invoice";
        }
        return "redirect:/invoices/list";
    }

    @PutMapping("/{id}")
    public String updateInvoice(@PathVariable Long id, @ModelAttribute Invoice invoice) {
        invoiceService.updateInvoice(id, invoice);
        return "redirect:/invoices/list";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/invoices/list";
    }

    @GetMapping("/{id}/details")
    public String viewInvoiceDetails(@PathVariable Long id, Model model) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy hh:mm a");
            String formattedDate = invoice.get().getDate().format(formatter);
            List<Map<String, Object>> invoiceItemsWithProductInfo = invoice.get().getInvoiceItems().stream()
                    .map(invoiceItem -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("productName", invoiceItem.getProduct().getName());
                        itemMap.put("productDesc", invoiceItem.getProduct().getDescription());
                        itemMap.put("productSerialNumber", invoiceItem.getSerialNumber());
                        itemMap.put("productColour", invoiceItem.getProduct().getColour());
                        itemMap.put("productSize", invoiceItem.getProduct().getSize());
                        itemMap.put("productPrintType", invoiceItem.getPrintType());
                        itemMap.put("productPrintedSide", invoiceItem.getPrintedSide());
                        itemMap.put("productPrice", invoiceItem.getProduct().getPrice());
                        itemMap.put("quantity", invoiceItem.getQuantity());
                        itemMap.put("subtotal", invoiceItem.getSubtotal());
                        return itemMap;
                    })
                    .toList();
            model.addAttribute("invoice", invoice.get());
            model.addAttribute("invoiceTotalAmount", invoice.get().getTotalAmount());
            model.addAttribute("invoiceDiscountAmount", invoice.get().getDiscountAmount());
            model.addAttribute("invoiceItems", invoiceItemsWithProductInfo);
            model.addAttribute("admin", adminService.getCurrentAdmin());
            model.addAttribute("formattedDate", formattedDate);
            return "invoice_details";
        }
        return "redirect:/invoices/list";
    }

}
