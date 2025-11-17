package com.order.service;

import com.order.entity.Order;
import com.order.entity.OrderItem;
import com.order.entity.Product;
import com.order.entity.ShoppingCart;
import com.order.entity.Wallet;
import com.order.enums.OrderStatus;
import com.order.enums.PaymentStatus;
import com.order.repository.OrderRepository;
import com.order.repository.ProductRepository;
import com.order.repository.ShoppingCartRepository;
import com.order.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单Service类
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private DeliveryFeeService deliveryFeeService;

    /**
     * 根据用户ID查找订单
     */
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedTimeDesc(userId);
    }

    /**
     * 根据订单号查找订单
     */
    public Order findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo).orElse(null);
    }

    /**
     * 根据ID查找订单
     */
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    /**
     * 根据状态查找订单
     */
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatusOrderByCreatedTimeDesc(status);
    }

    /**
     * 查找所有订单
     */
    public List<Order> findAllOrders() {
        return orderRepository.findAllOrders();
    }

    /**
     * 创建订单（从购物车）
     */
    @Transactional
    public Order createOrderFromCart(Long userId, String remark, String deliveryAddress, String contactName, String contactPhone) {
        // 获取购物车商品
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法下单");
        }

        // 验证收货地址信息
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            throw new RuntimeException("收货地址不能为空");
        }
        if (contactName == null || contactName.trim().isEmpty()) {
            throw new RuntimeException("联系人姓名不能为空");
        }
        if (contactPhone == null || contactPhone.trim().isEmpty()) {
            throw new RuntimeException("联系人电话不能为空");
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStatus("pending");
        order.setRemark(remark);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactName(contactName);
        order.setContactPhone(contactPhone);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 处理每个购物车商品
        for (ShoppingCart cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));

            // 检查库存
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品 " + product.getName() + " 库存不足");
            }

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setImage(product.getImageUrl());

            // 计算小计
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(subtotal);

            // 减少库存
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        
        // 计算配送费
        BigDecimal deliveryFee = deliveryFeeService.calculateDeliveryFee(order);
        order.setDeliveryFee(deliveryFee);
        
        // 实际金额 = 商品总金额 + 配送费 - 折扣
        BigDecimal actualAmount = totalAmount.add(deliveryFee).subtract(order.getDiscountAmount());
        order.setActualAmount(actualAmount);

        // 先保存订单以获取ID
        Order savedOrder = orderRepository.save(order);

        // 设置订单项的订单关联并保存
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
        }
        savedOrder.setOrderItems(orderItems);

        // 清空购物车
        shoppingCartRepository.deleteByUserId(userId);

        return savedOrder;
    }

    /**
     * 支付订单
     */
    @Transactional
    public Order payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }

        // 检查钱包余额是否充足
        if (!walletService.hasEnoughBalance(order.getUserId(), order.getActualAmount())) {
            throw new RuntimeException("钱包余额不足，当前余额：" + walletService.getBalance(order.getUserId()) + "元，需要：" + order.getActualAmount() + "元");
        }

        // 使用钱包余额支付
        boolean paymentSuccess = walletService.consume(order.getUserId(), order.getActualAmount());
        if (!paymentSuccess) {
            throw new RuntimeException("支付失败，请检查钱包余额");
        }

        // 更新订单状态为已支付
        order.setStatus("paid");
        order.setPaymentStatus(PaymentStatus.PAID.getCode());

        return orderRepository.save(order);
    }

    /**
     * 更新订单状态
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        order.setStatus(status);
        
        // 如果是取消订单，恢复库存
        if ("cancelled".equals(status)) {
            restoreStock(order);
        }

        return orderRepository.save(order);
    }

    /**
     * 取消订单
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        updateOrderStatus(orderId, "cancelled");
    }

    /**
     * 完成订单
     */
    // 删除重复的completeOrder方法，使用下面返回Order的新版本

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 恢复库存
     */
    private void restoreStock(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProductId())
                    .orElse(null);
            
            if (product != null) {
                product.setStock(product.getStock() + orderItem.getQuantity());
                productRepository.save(product);
            }
        }
    }

    /**
     * 根据状态查找订单（带参数）
     */
    public List<Order> findByStatusOrderByCreatedTimeDesc(String status) {
        if (status == null || status.isEmpty()) {
            return orderRepository.findByStatusOrderByCreatedTimeDesc("pending");
        }
        return orderRepository.findByStatusOrderByCreatedTimeDesc(status);
    }

    /**
     * 用户取消订单
     */
    @Transactional
    public Order cancelOrderByUser(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作此订单");
        }

        // 检查订单状态是否可以取消
        OrderStatus currentStatus = OrderStatus.fromCode(order.getStatus());
        if (currentStatus != OrderStatus.PENDING && currentStatus != OrderStatus.CONFIRMED) {
            throw new RuntimeException("当前订单状态不允许取消");
        }

        // 如果已支付，需要退款
        if (PaymentStatus.PAID.getCode().equals(order.getPaymentStatus())) {
            // 退款到钱包
            Wallet wallet = walletService.recharge(order.getUserId(), order.getActualAmount());
            if (wallet == null) {
                throw new RuntimeException("退款失败，请联系客服");
            }
            order.setPaymentStatus(PaymentStatus.REFUNDED.getCode());
        }

        // 更新订单状态为已取消
        order.setStatus(OrderStatus.CANCELLED.getCode());
        
        // 恢复商品库存
        restoreStock(order);

        return orderRepository.save(order);
    }

    /**
     * 商家确认订单
     */
    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        OrderStatus currentStatus = OrderStatus.fromCode(order.getStatus());
        if (currentStatus != OrderStatus.PENDING && currentStatus != OrderStatus.PAID) {
            throw new RuntimeException("只有待处理或已支付的订单才能确认");
        }

        // 检查支付状态，只有已支付的订单才能确认
        if (!PaymentStatus.PAID.getCode().equals(order.getPaymentStatus())) {
            throw new RuntimeException("订单未支付，无法确认接单");
        }

        order.setStatus(OrderStatus.CONFIRMED.getCode());
        return orderRepository.save(order);
    }

    /**
     * 商家拒绝订单
     */
    @Transactional
    public Order rejectOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        // 如果已支付，需要退款
        if (PaymentStatus.PAID.getCode().equals(order.getPaymentStatus())) {
            // 退款到钱包
            Wallet wallet = walletService.recharge(order.getUserId(), order.getActualAmount());
            if (wallet == null) {
                throw new RuntimeException("退款失败，请联系客服");
            }
            order.setPaymentStatus(PaymentStatus.REFUNDED.getCode());
        }

        // 更新订单状态为已拒绝
        order.setStatus(OrderStatus.REJECTED.getCode());
        if (reason != null && !reason.trim().isEmpty()) {
            order.setRemark("拒绝原因：" + reason);
        }

        // 恢复商品库存
        restoreStock(order);

        return orderRepository.save(order);
    }

    /**
     * 订单开始配送
     */
    @Transactional
    public Order startDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        OrderStatus currentStatus = OrderStatus.fromCode(order.getStatus());
        if (currentStatus != OrderStatus.CONFIRMED && currentStatus != OrderStatus.PREPARING) {
            throw new RuntimeException("只有已确认或准备中的订单才能开始配送");
        }

        order.setStatus(OrderStatus.DELIVERING.getCode());
        return orderRepository.save(order);
    }

    /**
     * 订单送达
     */
    @Transactional
    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        OrderStatus currentStatus = OrderStatus.fromCode(order.getStatus());
        if (currentStatus != OrderStatus.DELIVERING) {
            throw new RuntimeException("只有配送中的订单才能标记为已送达");
        }

        order.setStatus(OrderStatus.DELIVERED.getCode());
        return orderRepository.save(order);
    }

    /**
     * 完成订单（用户确认收货后）
     */
    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        OrderStatus currentStatus = OrderStatus.fromCode(order.getStatus());
        if (currentStatus != OrderStatus.DELIVERED) {
            throw new RuntimeException("只有已送达的订单才能完成");
        }

        order.setStatus(OrderStatus.COMPLETED.getCode());
        return orderRepository.save(order);
    }

    /**
     * 将订单商品重新加入购物车
     */
    @Transactional
    public int addOrderItemsToCart(Order order, Long userId) {
        int addedCount = 0;
        
        // 清空用户现有购物车（可选，根据业务需求）
        // shoppingCartRepository.deleteByUserId(userId);
        
        for (OrderItem orderItem : order.getOrderItems()) {
            // 检查商品是否还存在且可用
            Product product = productRepository.findById(orderItem.getProductId()).orElse(null);
            if (product == null || product.getStatus() != 1) {
                continue; // 跳过不存在或已下架的商品
            }
            
            // 检查库存
            if (product.getStock() < orderItem.getQuantity()) {
                continue; // 跳过库存不足的商品
            }
            
            // 检查购物车中是否已有该商品
            ShoppingCart existingCartItem = shoppingCartRepository
                    .findByUserIdAndProductId(userId, orderItem.getProductId());
            
            if (existingCartItem != null) {
                // 更新现有购物车项的数量
                int newQuantity = existingCartItem.getQuantity() + orderItem.getQuantity();
                existingCartItem.setQuantity(newQuantity);
                shoppingCartRepository.save(existingCartItem);
            } else {
                // 创建新的购物车项
                ShoppingCart cartItem = new ShoppingCart();
                cartItem.setUserId(userId);
                cartItem.setProductId(orderItem.getProductId());
                cartItem.setQuantity(orderItem.getQuantity());
                shoppingCartRepository.save(cartItem);
            }
            
            addedCount++;
        }
        
        return addedCount;
    }

    /**
     * 获取商家待处理订单列表
     */
    public List<Order> getPendingOrdersForMerchant() {
        return orderRepository.findByStatusOrderByCreatedTimeDesc(OrderStatus.PENDING.getCode());
    }

    /**
     * 获取商家所有订单列表
     */
    public List<Order> getAllOrdersForMerchant() {
        return orderRepository.findAllOrders();
    }

    public DeliveryFeeService getDeliveryFeeService() {
        return deliveryFeeService;
    }

    public WalletService getWalletService() {
        return walletService;
    }
}