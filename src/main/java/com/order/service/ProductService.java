package com.order.service;

import com.order.entity.Product;
import com.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findAvailableProducts();
    }
    
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findAvailableProductsByCategory(categoryId);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public boolean checkStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        return product != null && product.getStock() >= quantity;
    }
    
    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            product.setSales(product.getSales() + quantity);
            productRepository.save(product);
        }
    }
}