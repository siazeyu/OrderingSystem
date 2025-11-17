package com.order.service;

import com.order.entity.User;
import com.order.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户Service类
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据用户名查找用户
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * 根据手机号查找用户
     */
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }

    /**
     * 根据ID查找用户
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 保存用户
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 检查手机号是否存在
     */
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 用户注册
     */
    public User register(String username, String phone, String password) {
        // 检查用户名是否已存在
        if (existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (existsByPhone(phone)) {
            throw new RuntimeException("手机号已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password); // 实际项目中应该加密
        user.setStatus(1); // 1表示active状态

        return save(user);
    }

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            user = findByPhone(username);
        }

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!password.equals(user.getPassword())) { // 实际项目中应该使用加密比较
            throw new RuntimeException("密码错误");
        }

        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new RuntimeException("账户已被禁用");
        }

        return user;
    }
}