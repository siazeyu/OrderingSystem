package com.order.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet")
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "frozen_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal frozenBalance;
    
    @Column(name = "total_recharge", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalRecharge;
    
    @Column(name = "total_consumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalConsumption;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 构造函数
    public Wallet() {
        this.balance = BigDecimal.ZERO;
        this.frozenBalance = BigDecimal.ZERO;
        this.totalRecharge = BigDecimal.ZERO;
        this.totalConsumption = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Wallet(Long userId) {
        this();
        this.userId = userId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }
    
    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getTotalRecharge() {
        return totalRecharge;
    }
    
    public void setTotalRecharge(BigDecimal totalRecharge) {
        this.totalRecharge = totalRecharge;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getTotalConsumption() {
        return totalConsumption;
    }
    
    public void setTotalConsumption(BigDecimal totalConsumption) {
        this.totalConsumption = totalConsumption;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 业务方法
    public void recharge(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("充值金额必须大于0");
        }
        this.balance = this.balance.add(amount);
        this.totalRecharge = this.totalRecharge.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean consume(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("消费金额必须大于0");
        }
        if (this.balance.compareTo(amount) < 0) {
            return false; // 余额不足
        }
        this.balance = this.balance.subtract(amount);
        this.totalConsumption = this.totalConsumption.add(amount);
        this.updatedAt = LocalDateTime.now();
        return true;
    }
    
    public void freeze(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("冻结金额必须大于0");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("余额不足，无法冻结");
        }
        this.balance = this.balance.subtract(amount);
        this.frozenBalance = this.frozenBalance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unfreeze(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("解冻金额必须大于0");
        }
        if (this.frozenBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("冻结余额不足，无法解冻");
        }
        this.frozenBalance = this.frozenBalance.subtract(amount);
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getAvailableBalance() {
        return balance;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}