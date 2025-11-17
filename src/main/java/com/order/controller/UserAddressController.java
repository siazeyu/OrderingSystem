package com.order.controller;

import com.order.entity.UserAddress;
import com.order.service.UserAddressService;
import com.order.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户收货地址控制器 - RESTful风格
 */
@RestController
@RequestMapping("/users/{userId}/addresses")
public class UserAddressController {
    
    @Autowired
    private UserAddressService userAddressService;
    
    /**
     * 获取用户的所有收货地址
     */
    @GetMapping
    public Result<List<UserAddress>> getUserAddresses(@PathVariable Long userId) {
        try {
            List<UserAddress> addresses = userAddressService.getUserAddresses(userId);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.error("获取地址列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户的默认收货地址
     */
    @GetMapping("/default")
    public Result<UserAddress> getDefaultAddress(@PathVariable Long userId) {
        try {
            Optional<UserAddress> defaultAddress = userAddressService.getDefaultAddress(userId);
            if (defaultAddress.isPresent()) {
                return Result.success(defaultAddress.get());
            } else {
                return Result.error("暂无默认收货地址");
            }
        } catch (Exception e) {
            return Result.error("获取默认地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取单个地址详情
     */
    @GetMapping("/{addressId}")
    public Result<UserAddress> getAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        try {
            Optional<UserAddress> address = userAddressService.getAddress(userId, addressId);
            if (address.isPresent()) {
                return Result.success(address.get());
            } else {
                return Result.error("地址不存在");
            }
        } catch (Exception e) {
            return Result.error("获取地址详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加收货地址
     */
    @PostMapping
    public Result<UserAddress> addAddress(@PathVariable Long userId, @RequestBody UserAddress address) {
        try {
            // 确保地址关联到正确的用户
            address.setUserId(userId);
            UserAddress savedAddress = userAddressService.addAddress(address);
            return Result.success(savedAddress);
        } catch (Exception e) {
            return Result.error("添加地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新收货地址
     * PUT /users/{userId}/addresses/{addressId}
     */
    @PutMapping("/{addressId}")
    public Result<UserAddress> updateAddress(@PathVariable Long userId, 
                                                 @PathVariable Long addressId, 
                                                 @RequestBody UserAddress addressDetails) {
        try {
            UserAddress updatedAddress = userAddressService.updateAddress(userId, addressId, addressDetails);
            return Result.success(updatedAddress);
        } catch (Exception e) {
            return Result.error("更新地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 部分更新收货地址（用于设置默认地址等场景）
     * PATCH /users/{userId}/addresses/{addressId}
     */
    @PatchMapping("/{addressId}")
    public Result<UserAddress> patchAddress(@PathVariable Long userId, 
                                                 @PathVariable Long addressId, 
                                                 @RequestBody Map<String, Object> updates) {
        try {
            // 获取现有地址
            Optional<UserAddress> existingAddressOpt = userAddressService.getAddress(userId, addressId);
            if (!existingAddressOpt.isPresent()) {
                return Result.error("地址不存在");
            }
            
            UserAddress existingAddress = existingAddressOpt.get();
            
            // 处理设置默认地址的特殊情况
            if (updates.containsKey("isDefault") && Boolean.TRUE.equals(updates.get("isDefault"))) {
                userAddressService.setDefaultAddress(userId, addressId);
                existingAddress.setIsDefault(true);
            }
            
            // 处理其他字段更新
            if (updates.containsKey("addressTag")) {
                existingAddress.setAddressTag((String) updates.get("addressTag"));
            }
            if (updates.containsKey("deliveryAddress")) {
                existingAddress.setDeliveryAddress((String) updates.get("deliveryAddress"));
            }
            if (updates.containsKey("contactName")) {
                existingAddress.setContactName((String) updates.get("contactName"));
            }
            if (updates.containsKey("contactPhone")) {
                existingAddress.setContactPhone((String) updates.get("contactPhone"));
            }
            
            UserAddress updatedAddress = userAddressService.updateAddress(userId, addressId, existingAddress);
            return Result.success(updatedAddress);
        } catch (Exception e) {
            return Result.error("部分更新地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除收货地址
     * DELETE /users/{userId}/addresses/{addressId}
     */
    @DeleteMapping("/{addressId}")
    public Result<String> deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        try {
            userAddressService.deleteAddress(userId, addressId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 设置默认地址 - 专用接口
     * PATCH /users/{userId}/addresses/{addressId}/default
     */
    @PatchMapping("/{addressId}/default")
    public Result<String> setDefaultAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        try {
            userAddressService.setDefaultAddress(userId, addressId);
            return Result.success("设置默认地址成功");
        } catch (Exception e) {
            return Result.error("设置默认地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除地址
     * DELETE /users/{userId}/addresses/batch
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteAddresses(@PathVariable Long userId, 
                                                   @RequestBody Map<String, List<Long>> request) {
        try {
            List<Long> addressIds = request.get("addressIds");
            if (addressIds == null || addressIds.isEmpty()) {
                return Result.error("请选择要删除的地址");
            }
            
            for (Long addressId : addressIds) {
                userAddressService.deleteAddress(userId, addressId);
            }
            
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }
}