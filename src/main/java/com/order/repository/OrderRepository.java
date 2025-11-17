package com.order.repository;

import com.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUserIdOrderByCreatedTimeDesc(Long userId);
    
    Optional<Order> findByOrderNo(String orderNo);

    
    @Query("SELECT o FROM Order o ORDER BY o.createdTime DESC")
    List<Order> findAllOrders();
    
    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.createdTime DESC")
    List<Order> findByStatusOrderByCreatedTimeDesc(@Param("status") String status);
}