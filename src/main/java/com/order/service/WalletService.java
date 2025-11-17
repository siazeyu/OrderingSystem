package com.order.service;

import com.order.entity.Wallet;
import com.order.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class WalletService {
    
    @Autowired
    private WalletRepository walletRepository;
    
    /**
     * 获取用户钱包，如果不存在则自动创建
     */
    public Wallet getUserWallet(Long userId) {
        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        if (walletOpt.isPresent()) {
            return walletOpt.get();
        } else {
            // 自动创建钱包
            Wallet wallet = new Wallet(userId);
            return walletRepository.save(wallet);
        }
    }
    
    /**
     * 钱包充值（模拟充值）
     */
    public Wallet recharge(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("充值金额必须大于0");
        }
        
        // 模拟充值限制：单次充值不超过1000元
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            throw new IllegalArgumentException("单次充值金额不能超过1000元");
        }
        
        Wallet wallet = getUserWallet(userId);
        wallet.recharge(amount);
        return walletRepository.save(wallet);
    }
    
    /**
     * 钱包消费
     */
    public boolean consume(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("消费金额必须大于0");
        }
        
        Wallet wallet = getUserWallet(userId);
        boolean success = wallet.consume(amount);
        if (success) {
            walletRepository.save(wallet);
        }
        return success;
    }
    
    /**
     * 冻结余额
     */
    public void freezeBalance(Long userId, BigDecimal amount) {
        Wallet wallet = getUserWallet(userId);
        wallet.freeze(amount);
        walletRepository.save(wallet);
    }
    
    /**
     * 解冻余额
     */
    public void unfreezeBalance(Long userId, BigDecimal amount) {
        Wallet wallet = getUserWallet(userId);
        wallet.unfreeze(amount);
        walletRepository.save(wallet);
    }
    
    /**
     * 检查余额是否充足
     */
    public boolean hasEnoughBalance(Long userId, BigDecimal amount) {
        Wallet wallet = getUserWallet(userId);
        return wallet.getBalance().compareTo(amount) >= 0;
    }
    
    /**
     * 获取钱包余额
     */
    public BigDecimal getBalance(Long userId) {
        Wallet wallet = getUserWallet(userId);
        return wallet.getBalance();
    }
    
    /**
     * 获取钱包详细信息
     */
    public Wallet getWalletInfo(Long userId) {
        return getUserWallet(userId);
    }
    
    /**
     * 模拟充值 - 快捷充值
     */
    public Wallet quickRecharge(Long userId, String amount) {
        try {
            BigDecimal rechargeAmount = new BigDecimal(amount);
            return recharge(userId, rechargeAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的充值金额");
        }
    }
    
    /**
     * 删除用户钱包（管理员功能）
     */
    @Transactional
    public void deleteUserWallet(Long userId) {
        walletRepository.deleteByUserId(userId);
    }
    
    /**
     * 检查用户是否有钱包
     */
    public boolean userHasWallet(Long userId) {
        return walletRepository.existsByUserId(userId);
    }
}