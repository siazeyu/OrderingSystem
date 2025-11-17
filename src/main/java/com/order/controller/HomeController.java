package com.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页控制器
 */
@Controller
public class HomeController {

    /**
     * 主页
     */
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }
}