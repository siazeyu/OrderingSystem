package com.order.repository;

import com.order.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    
    List<ShoppingCart> findByUserId(Long userId);
    
    ShoppingCart findByUserIdAndProductId(Long userId, Long productId);
    
    void deleteByUserIdAndProductId(Long userId, Long productId);
    
    void deleteByUserId(Long userId);
    
    @Modifying
    @Query("UPDATE ShoppingCart sc SET sc.quantity = :quantity WHERE sc.userId = :userId AND sc.productId = :productId")
    int updateQuantity(@Param("userId") Long userId, @Param("productId") Long productId, @Param("quantity") Integer quantity);
}