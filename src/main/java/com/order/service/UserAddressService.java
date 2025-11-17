package com.order.service;

import com.order.entity.UserAddress;
import com.order.repository.UserAddressRepository;
import com.order.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户收货地址服务
 */
@Service
public class UserAddressService {
    
    @Autowired
    private UserAddressRepository userAddressRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取用户的所有收货地址
     */
    public List<UserAddress> getUserAddresses(Long userId) {
        return userAddressRepository.findByUserIdOrderByIsDefaultDescCreateTimeDesc(userId);
    }
    
    /**
     * 获取用户的默认收货地址
     */
    public Optional<UserAddress> getDefaultAddress(Long userId) {
        return userAddressRepository.findByUserIdAndIsDefaultTrue(userId);
    }
    
    /**
     * 添加收货地址
     */
    @Transactional
    public UserAddress addAddress(UserAddress address) {
        // 如果设置为默认地址，先将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            userAddressRepository.setAllAddressesNonDefault(address.getUserId());
        }
        
        // 如果是第一个地址，自动设为默认
        long addressCount = userAddressRepository.countByUserId(address.getUserId());
        if (addressCount == 0) {
            address.setIsDefault(true);
        }
        
        UserAddress savedAddress = userAddressRepository.save(address);
        
        // 如果是默认地址，更新用户表的默认地址ID
        if (Boolean.TRUE.equals(savedAddress.getIsDefault())) {
            userRepository.updateDefaultAddressId(address.getUserId(), savedAddress.getId());
        }
        
        return savedAddress;
    }
    
    /**
     * 更新收货地址
     */
    @Transactional
    public UserAddress updateAddress(Long userId, Long addressId, UserAddress addressDetails) {
        Optional<UserAddress> optionalAddress = userAddressRepository.findByUserIdAndId(userId, addressId);
        if (!optionalAddress.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        UserAddress address = optionalAddress.get();
        address.setDeliveryAddress(addressDetails.getDeliveryAddress());
        address.setContactName(addressDetails.getContactName());
        address.setContactPhone(addressDetails.getContactPhone());
        address.setAddressTag(addressDetails.getAddressTag());
        
        // 如果设置为默认地址，先将其他地址设为非默认
        if (Boolean.TRUE.equals(addressDetails.getIsDefault()) && !Boolean.TRUE.equals(address.getIsDefault())) {
            userAddressRepository.setAllAddressesNonDefault(userId);
            address.setIsDefault(true);
            userRepository.updateDefaultAddressId(userId, addressId);
        }
        
        return userAddressRepository.save(address);
    }
    
    /**
     * 删除收货地址
     */
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Optional<UserAddress> optionalAddress = userAddressRepository.findByUserIdAndId(userId, addressId);
        if (!optionalAddress.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        UserAddress address = optionalAddress.get();
        
        // 如果删除的是默认地址，需要重新设置默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            userAddressRepository.deleteById(addressId);
            
            // 查找其他地址，将第一个设为默认
            List<UserAddress> remainingAddresses = userAddressRepository.findByUserIdOrderByIsDefaultDescCreateTimeDesc(userId);
            if (!remainingAddresses.isEmpty()) {
                UserAddress newDefaultAddress = remainingAddresses.get(0);
                newDefaultAddress.setIsDefault(true);
                userAddressRepository.save(newDefaultAddress);
                userRepository.updateDefaultAddressId(userId, newDefaultAddress.getId());
            } else {
                // 没有其他地址了，清空用户的默认地址ID
                userRepository.updateDefaultAddressId(userId, null);
            }
        } else {
            userAddressRepository.deleteById(addressId);
        }
    }
    
    /**
     * 设置默认地址
     */
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        Optional<UserAddress> optionalAddress = userAddressRepository.findByUserIdAndId(userId, addressId);
        if (!optionalAddress.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        // 将所有地址设为非默认
        userAddressRepository.setAllAddressesNonDefault(userId);
        
        // 设置指定地址为默认
        UserAddress address = optionalAddress.get();
        address.setIsDefault(true);
        userAddressRepository.save(address);
        
        // 更新用户表的默认地址ID
        userRepository.updateDefaultAddressId(userId, addressId);
    }
    
    /**
     * 部分更新收货地址
     */
    @Transactional
    public UserAddress patchAddress(Long userId, Long addressId, Map<String, Object> updates) {
        Optional<UserAddress> optionalAddress = userAddressRepository.findByUserIdAndId(userId, addressId);
        if (!optionalAddress.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        UserAddress address = optionalAddress.get();
        
        // 部分更新字段
        if (updates.containsKey("deliveryAddress")) {
            address.setDeliveryAddress((String) updates.get("deliveryAddress"));
        }
        if (updates.containsKey("contactName")) {
            address.setContactName((String) updates.get("contactName"));
        }
        if (updates.containsKey("contactPhone")) {
            address.setContactPhone((String) updates.get("contactPhone"));
        }
        if (updates.containsKey("addressTag")) {
            address.setAddressTag((String) updates.get("addressTag"));
        }
        if (updates.containsKey("isDefault")) {
            Boolean isDefault = (Boolean) updates.get("isDefault");
            // 如果设置为默认地址，先将其他地址设为非默认
            if (Boolean.TRUE.equals(isDefault) && !Boolean.TRUE.equals(address.getIsDefault())) {
                userAddressRepository.setAllAddressesNonDefault(userId);
                address.setIsDefault(true);
                userRepository.updateDefaultAddressId(userId, addressId);
            }
        }
        
        return userAddressRepository.save(address);
    }
    
    /**
     * 获取单个地址详情
     */
    public Optional<UserAddress> getAddress(Long userId, Long addressId) {
        return userAddressRepository.findByUserIdAndId(userId, addressId);
    }
}