package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.dao.AdminDAO;
import com.muhammadminhaz.inventorymanagementsystem.dao.ProductDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductDAO productDAO;
    private final AdminDAO adminDAO;
    private final AdminService adminService;

    @Autowired
    public ProductService(ProductDAO productDAO, AdminDAO adminDAO, AdminService adminService) {
        this.productDAO = productDAO;
        this.adminDAO = adminDAO;
        this.adminService = adminService;
    }

    public Product createProduct(Product product) {
        Admin currentAdmin = adminService.getCurrentAdmin();
        product.setAdmin(currentAdmin);
        return productDAO.save(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productDAO.findById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productDAO.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setColour(productDetails.getColour());
            product.setSize(productDetails.getSize());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            return productDAO.save(product);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productDAO.deleteById(id);
    }
}
