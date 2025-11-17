package com.order.controller;

import com.order.common.Result;
import com.order.entity.Order;
import com.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 获取所有订单列表
     */
    @GetMapping("/list")
    public Result<List<Order>> list(){
        try {
            List<Order> orders = orderService.findAllOrders();
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("获取订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("获取订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据订单号获取订单详情
     */
    @GetMapping("/orderNo/{orderNo}")
    public Result<Order> getOrderByOrderNo(@PathVariable String orderNo) {
        try {
            Order order = orderService.findByOrderNo(orderNo);
            if (order != null) {
                return Result.success(order);
            } else {
                return Result.error("订单不存在");
            }
        } catch (Exception e) {
            return Result.error("获取订单详情失败：" + e.getMessage());
        }
    }

    /**
     * 根据状态获取订单列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> orders = orderService.findByStatus(status);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("获取订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有订单列表
     */
    @GetMapping("/all")
    public Result<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderService.findAllOrders();
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("获取订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 创建订单（从购物车）
     */
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestParam Long userId, 
                                    @RequestParam(required = false) String remark,
                                    @RequestParam String deliveryAddress,
                                    @RequestParam String contactName,
                                    @RequestParam String contactPhone) {
        try {
            Order order = orderService.createOrderFromCart(userId, remark, deliveryAddress, contactName, contactPhone);
            return Result.success(order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 支付订单
     */
    @PostMapping("/{orderId}/pay")
    public Result<Order> payOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.payOrder(orderId);
            return Result.success("支付成功", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("支付失败：" + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @PutMapping("/{orderId}/cancel")
    public Result<String> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return Result.success("订单已取消");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("取消订单失败：" + e.getMessage());
        }
    }

    /**
     * 完成订单
     */
    @PutMapping("/{orderId}/complete")
    public Result<String> completeOrder(@PathVariable Long orderId) {
        try {
            orderService.completeOrder(orderId);
            return Result.success("订单已完成");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("完成订单失败：" + e.getMessage());
        }
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{orderId}/status")
    public Result<Order> updateOrderStatus(@PathVariable Long orderId, 
                                          @RequestParam String status) {
        try {
            Order order = orderService.updateOrderStatus(orderId, status);
            return Result.success(order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("更新订单状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public Result<Order> getOrderById(@PathVariable Long orderId) {
        try {
            // 需要在OrderService中添加findById方法
            Order order = orderService.findById(orderId);
            if (order != null) {
                return Result.success(order);
            } else {
                return Result.error("订单不存在");
            }
        } catch (Exception e) {
            return Result.error("获取订单详情失败：" + e.getMessage());
        }
    }

    /**
     * 再来一单（根据原订单创建新订单）
     */
    @PostMapping("/reorder/{orderId}")
    public Result<String> reorder(@PathVariable Long orderId, @RequestParam Long userId) {
        try {
            Order originalOrder = orderService.findById(orderId);
            if (originalOrder == null) {
                return Result.error("原订单不存在");
            }
            
            // 验证订单是否属于当前用户
            if (!originalOrder.getUserId().equals(userId)) {
                return Result.error("无权限操作此订单");
            }
            
            // 将原订单商品重新加入购物车
            int addedCount = orderService.addOrderItemsToCart(originalOrder, userId);
            
            return Result.success("成功将 " + addedCount + " 件商品加入购物车");
        } catch (Exception e) {
            return Result.error("再来一单失败：" + e.getMessage());
        }
    }

    // ========== 新增的订单管理API ==========

    /**
     * 用户取消订单（带用户验证）
     */
    @PutMapping("/{orderId}/cancel/user")
    public Result<Order> cancelOrderByUser(@PathVariable Long orderId, @RequestParam Long userId) {
        try {
            Order order = orderService.cancelOrderByUser(orderId, userId);
            return Result.success("订单已取消，退款已处理", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("取消订单失败：" + e.getMessage());
        }
    }

    /**
     * 商家确认订单
     */
    @PutMapping("/{orderId}/confirm")
    public Result<Order> confirmOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.confirmOrder(orderId);
            return Result.success("订单已确认", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("确认订单失败：" + e.getMessage());
        }
    }

    /**
     * 商家拒绝订单
     */
    @PutMapping("/{orderId}/reject")
    public Result<Order> rejectOrder(@PathVariable Long orderId, @RequestParam(required = false) String reason) {
        try {
            Order order = orderService.rejectOrder(orderId, reason);
            return Result.success("订单已拒绝，退款已处理", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("拒绝订单失败：" + e.getMessage());
        }
    }

    /**
     * 开始配送
     */
    @PutMapping("/{orderId}/start-delivery")
    public Result<Order> startDelivery(@PathVariable Long orderId) {
        try {
            Order order = orderService.startDelivery(orderId);
            return Result.success("订单开始配送", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("开始配送失败：" + e.getMessage());
        }
    }

    /**
     * 订单送达
     */
    @PutMapping("/{orderId}/deliver")
    public Result<Order> deliverOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.deliverOrder(orderId);
            return Result.success("订单已送达", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("送达订单失败：" + e.getMessage());
        }
    }

    /**
     * 获取商家待处理订单列表
     */
    @GetMapping("/merchant/pending")
    public Result<List<Order>> getPendingOrdersForMerchant() {
        try {
            List<Order> orders = orderService.getPendingOrdersForMerchant();
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("获取待处理订单失败：" + e.getMessage());
        }
    }

}