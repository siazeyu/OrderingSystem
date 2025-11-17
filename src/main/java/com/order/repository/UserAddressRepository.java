package com.order.repository;

import com.order.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户收货地址Repository
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    
    /**
     * 根据用户ID查找所有地址
     */
    List<UserAddress> findByUserIdOrderByIsDefaultDescCreateTimeDesc(Long userId);
    
    /**
     * 根据用户ID和地址ID查找地址
     */
    Optional<UserAddress> findByUserIdAndId(Long userId, Long addressId);
    
    /**
     * 根据用户ID查找默认地址
     */
    Optional<UserAddress> findByUserIdAndIsDefaultTrue(Long userId);
    
    /**
     * 根据用户ID设置所有地址为非默认
     */
    @Modifying
    @Query("UPDATE UserAddress ua SET ua.isDefault = false WHERE ua.userId = :userId")
    int setAllAddressesNonDefault(@Param("userId") Long userId);
    
    /**
     * 统计用户的地址数量
     */
    long countByUserId(Long userId);
    
    /**
     * 根据用户ID删除所有地址
     */
    @Modifying
    @Query("DELETE FROM UserAddress ua WHERE ua.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}