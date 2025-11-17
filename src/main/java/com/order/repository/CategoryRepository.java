package com.order.repository;

import com.order.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByStatus(Integer status);
    
    @Query("SELECT c FROM Category c WHERE c.status = 1 ORDER BY c.sortOrder ASC, c.id ASC")
    List<Category> findAvailableCategories();
}