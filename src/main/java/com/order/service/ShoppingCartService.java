package com.order.service;

import com.order.entity.Product;
import com.order.entity.ShoppingCart;
import com.order.repository.ProductRepository;
import com.order.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    
    @Autowired
    private ProductService productService;
    
    public List<ShoppingCart> getCartByUserId(Long userId) {
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUserId(userId);
        // 为每个购物车项设置商品信息
        for (ShoppingCart item : cartItems) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                item.setProduct(product);
            }
        }
        return cartItems;
    }
    
    @Transactional
    public ShoppingCart addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否存在且有库存
        Product product = productService.getProductById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        if (!productService.checkStock(productId, quantity)) {
            throw new RuntimeException("商品库存不足");
        }
        
        // 查找是否已在购物车中
        Optional<ShoppingCart> existingItem = Optional.ofNullable(
            shoppingCartRepository.findByUserIdAndProductId(userId, productId)
        );
        
        if (existingItem.isPresent()) {
            // 更新数量
            ShoppingCart cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (!productService.checkStock(productId, newQuantity)) {
                throw new RuntimeException("商品库存不足");
            }
            
            cartItem.setQuantity(newQuantity);
            return shoppingCartRepository.save(cartItem);
        } else {
            // 新增购物车项
            ShoppingCart cartItem = new ShoppingCart(userId, productId, quantity);
            return shoppingCartRepository.save(cartItem);
        }
    }
    
    @Transactional
    public ShoppingCart updateQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(userId, productId);
            return null;
        }
        
        // 检查库存
        if (!productService.checkStock(productId, quantity)) {
            throw new RuntimeException("商品库存不足");
        }
        
        ShoppingCart cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            return shoppingCartRepository.save(cartItem);
        }
        throw new RuntimeException("购物车中不存在该商品");
    }
    
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        shoppingCartRepository.deleteByUserIdAndProductId(userId, productId);
    }
    
    @Transactional
    public void clearCart(Long userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }
    
    public BigDecimal getCartTotal(Long userId) {
        List<ShoppingCart> cartItems = getCartByUserId(userId);
        BigDecimal total = BigDecimal.ZERO;
        
        for (ShoppingCart item : cartItems) {
            if (item.getProduct() != null) {
                total = total.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
            }
        }
        
        return total;
    }
    
    public int getCartItemCount(Long userId) {
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUserId(userId);
        return cartItems.stream().mapToInt(ShoppingCart::getQuantity).sum();
    }
}