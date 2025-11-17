package com.order.controller;

import com.order.entity.Wallet;
import com.order.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
@CrossOrigin(origins = "*")
public class WalletController {
    
    @Autowired
    private WalletService walletService;
    
    /**
     * 获取用户钱包信息
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserWallet(@PathVariable Long userId) {
        try {
            Wallet wallet = walletService.getWalletInfo(userId);
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取钱包信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取用户余额
     */
    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        try {
            BigDecimal balance = walletService.getBalance(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("balance", balance);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取余额失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 钱包充值
     */
    @PostMapping("/recharge")
    public ResponseEntity<?> recharge(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            Wallet wallet = walletService.recharge(userId, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "充值成功");
            response.put("wallet", wallet);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "充值失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 快捷充值（预设金额）
     */
    @PostMapping("/quick-recharge")
    public ResponseEntity<?> quickRecharge(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String amount = request.get("amount").toString();
            
            Wallet wallet = walletService.quickRecharge(userId, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "充值成功");
            response.put("wallet", wallet);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "充值失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 检查余额是否充足
     */
    @PostMapping("/check-balance")
    public ResponseEntity<?> checkBalance(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            boolean hasEnough = walletService.hasEnoughBalance(userId, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasEnoughBalance", hasEnough);
            response.put("currentBalance", walletService.getBalance(userId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "检查余额失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 钱包消费
     */
    @PostMapping("/consume")
    public ResponseEntity<?> consume(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            boolean success = walletService.consume(userId, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            if (success) {
                response.put("message", "消费成功");
                response.put("wallet", walletService.getWalletInfo(userId));
            } else {
                response.put("message", "余额不足");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "消费失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 冻结余额
     */
    @PostMapping("/freeze")
    public ResponseEntity<?> freezeBalance(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            walletService.freezeBalance(userId, amount);
            Wallet wallet = walletService.getWalletInfo(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "冻结成功");
            response.put("wallet", wallet);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "冻结失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 解冻余额
     */
    @PostMapping("/unfreeze")
    public ResponseEntity<?> unfreezeBalance(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            walletService.unfreezeBalance(userId, amount);
            Wallet wallet = walletService.getWalletInfo(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "解冻成功");
            response.put("wallet", wallet);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "解冻失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}