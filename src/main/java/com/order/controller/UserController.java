package com.order.controller;

import com.order.common.Result;
import com.order.entity.User;
import com.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestParam String username, 
                                @RequestParam String phone, 
                                @RequestParam String password) {
        try {
            User user = userService.register(username, phone, password);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestParam String username,
                             @RequestParam String password) {
        try {
            User user = userService.login(username, password);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return Result.success(user.get());
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username/{username}")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check/phone/{phone}")
    public Result<Boolean> checkPhone(@PathVariable String phone) {
        boolean exists = userService.existsByPhone(phone);
        return Result.success(exists);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            Optional<User> existingUser = userService.findById(id);
            if (!existingUser.isPresent()) {
                return Result.error("用户不存在");
            }

            User updatedUser = existingUser.get();
            updatedUser.setNickname(user.getNickname());
            updatedUser.setAvatar(user.getAvatar());

            User savedUser = userService.save(updatedUser);
            return Result.success(savedUser);
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/{id}/password")
    public Result<String> updatePassword(@PathVariable Long id, 
                                       @RequestParam String oldPassword, 
                                       @RequestParam String newPassword) {
        try {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return Result.error("用户不存在");
            }

            User user = userOptional.get();
            if (!oldPassword.equals(user.getPassword())) {
                return Result.error("原密码错误");
            }

            user.setPassword(newPassword); // 实际项目中应该加密
            userService.save(user);

            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error("密码修改失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息（表单参数）
     */
    @PostMapping("/update")
    public Result<User> updateUser(@RequestParam Long userId,
                                  @RequestParam(required = false) String nickname,
                                  @RequestParam(required = false) String avatar) {
        try {
            Optional<User> userOptional = userService.findById(userId);
            if (!userOptional.isPresent()) {
                return Result.error("用户不存在");
            }

            User user = userOptional.get();
            if (nickname != null) {
                user.setNickname(nickname);
            }
            if (avatar != null) {
                user.setAvatar(avatar);
            }

            User savedUser = userService.save(user);
            return Result.success(savedUser);
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 修改密码（表单参数）
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestParam Long userId,
                                        @RequestParam String currentPassword,
                                        @RequestParam String newPassword) {
        try {
            Optional<User> userOptional = userService.findById(userId);
            if (!userOptional.isPresent()) {
                return Result.error("用户不存在");
            }

            User user = userOptional.get();
            if (!currentPassword.equals(user.getPassword())) {
                return Result.error("当前密码错误");
            }

            user.setPassword(newPassword); // 实际项目中应该加密
            userService.save(user);

            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error("密码修改失败：" + e.getMessage());
        }
    }
}