package com.order.repository;

import com.order.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    /**
     * 根据用户ID查找钱包
     */
    Optional<Wallet> findByUserId(Long userId);
    
    /**
     * 根据用户ID查找钱包（返回实体，不存在时抛出异常）
     */
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId")
    Wallet getByUserId(@Param("userId") Long userId);
    
    /**
     * 检查用户是否已有钱包
     */
    boolean existsByUserId(Long userId);
    
    /**
     * 根据用户ID删除钱包
     */
    void deleteByUserId(Long userId);
}