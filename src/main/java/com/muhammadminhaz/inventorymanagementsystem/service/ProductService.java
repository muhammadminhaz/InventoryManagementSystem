package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.dao.ProductDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    @Autowired
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // Create a new product
    public Product createProduct(Product product) {
        return productDAO.save(product);
    }


    // Retrieve all products
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    // Retrieve a specific product by ID
    public Optional<Product> getProductById(Long id) {
        return productDAO.findById(id);
    }

    // Update an existing product
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
        return null; // or throw an exception
    }


    // Delete a product
    public void deleteProduct(Long id) {
        productDAO.deleteById(id);
    }
}
