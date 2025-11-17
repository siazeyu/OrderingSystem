package com.order.controller;

import com.order.common.Result;
import com.order.entity.ShoppingCart;
import com.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    
    @Autowired
    private ShoppingCartService shoppingCartService;
    
    @GetMapping("/list/{userId}")
    public Result<List<ShoppingCart>> getCartItems(@PathVariable Long userId) {
        List<ShoppingCart> cartItems = shoppingCartService.getCartByUserId(userId);
        return Result.success(cartItems);
    }
    
    @PostMapping("/add")
    public Result<ShoppingCart> addToCart(@RequestParam Long userId, 
                                         @RequestParam Long productId, 
                                         @RequestParam Integer quantity) {
        try {
            ShoppingCart cartItem = shoppingCartService.addToCart(userId, productId, quantity);
            return Result.success("添加到购物车成功", cartItem);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update")
    public Result<ShoppingCart> updateQuantity(@RequestParam Long userId, 
                                              @RequestParam Long productId, 
                                              @RequestParam Integer quantity) {
        try {
            ShoppingCart cartItem = shoppingCartService.updateQuantity(userId, productId, quantity);
            if (cartItem != null) {
                return Result.success("更新数量成功", cartItem);
            } else {
                // 当商品被移除时，返回一个空的ShoppingCart对象或者抛出异常
                return Result.error("商品已从购物车移除");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/remove")
    public Result<Void> removeFromCart(@RequestParam Long userId, 
                                      @RequestParam Long productId) {
        shoppingCartService.removeFromCart(userId, productId);
        return Result.success();
    }
    
    @DeleteMapping("/clear/{userId}")
    public Result<Void> clearCart(@PathVariable Long userId) {
        shoppingCartService.clearCart(userId);
        return Result.success();
    }
    
    @PostMapping("/migrate")
    public Result<Void> migrateGuestCart(@RequestParam Long userId, 
                                        @RequestBody Map<Long, Integer> cartData) {
        try {
            for (Map.Entry<Long, Integer> entry : cartData.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();
                shoppingCartService.addToCart(userId, productId, quantity);
            }
            return Result.success("购物车数据迁移成功", null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/total/{userId}")
    public Result<Map<String, Object>> getCartSummary(@PathVariable Long userId) {
        BigDecimal total = shoppingCartService.getCartTotal(userId);
        int itemCount = shoppingCartService.getCartItemCount(userId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", total);
        summary.put("itemCount", itemCount);
        
        return Result.success(summary);
    }
}