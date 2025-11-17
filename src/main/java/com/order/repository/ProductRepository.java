package com.order.repository;

import com.order.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategoryIdAndStatus(Long categoryId, Integer status);
    
    List<Product> findByStatus(Integer status);
    
    @Query("SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.sortOrder ASC, p.id DESC")
    List<Product> findAvailableProducts();
    
    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId AND p.status = 1 ORDER BY p.sortOrder ASC, p.id DESC")
    List<Product> findAvailableProductsByCategory(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.status = 1 ORDER BY p.sortOrder ASC, p.id DESC")
    List<Product> searchProducts(@Param("name") String name);
}