package com.order.repository;

import com.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhone(String phone);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhone(String phone);
    
    /**
     * 更新用户的默认收货地址ID
     */
    @Modifying
    @Query("UPDATE User u SET u.defaultAddressId = :addressId WHERE u.id = :userId")
    void updateDefaultAddressId(@Param("userId") Long userId, @Param("addressId") Long addressId);
}