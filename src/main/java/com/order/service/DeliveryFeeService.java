package com.order.service;

import com.order.entity.Order;
import com.order.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 配送费计算服务
 */
@Service
public class DeliveryFeeService {

    // 配送费配置
    private static final BigDecimal BASE_DELIVERY_FEE = new BigDecimal("5.00");
    private static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("100.00");
    private static final BigDecimal DISTANCE_FEE_PER_KM = new BigDecimal("2.00");
    
    /**
     * 计算配送费
     * @param order 订单对象
     * @param distance 配送距离（公里），如果为null则使用基础配送费
     * @return 配送费
     */
    public BigDecimal calculateDeliveryFee(Order order, Double distance) {
        // 如果订单金额满免配送费门槛，免配送费
        if (order.getTotalAmount().compareTo(FREE_DELIVERY_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal deliveryFee = BASE_DELIVERY_FEE;
        
        // 如果提供了距离，计算距离费用
        if (distance != null && distance > 0) {
            BigDecimal distanceFee = new BigDecimal(distance).multiply(DISTANCE_FEE_PER_KM);
            deliveryFee = deliveryFee.add(distanceFee);
        }
        
        return deliveryFee.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算配送费（使用基础配送费）
     * @param order 订单对象
     * @return 配送费
     */
    public BigDecimal calculateDeliveryFee(Order order) {
        return calculateDeliveryFee(order, null);
    }
    
    /**
     * 检查是否免配送费
     * @param orderAmount 订单金额
     * @return 是否免配送费
     */
    public boolean isFreeDelivery(BigDecimal orderAmount) {
        return orderAmount.compareTo(FREE_DELIVERY_THRESHOLD) >= 0;
    }
    
    /**
     * 获取免配送费门槛
     * @return 免配送费门槛金额
     */
    public BigDecimal getFreeDeliveryThreshold() {
        return FREE_DELIVERY_THRESHOLD;
    }
    
    /**
     * 获取基础配送费
     * @return 基础配送费
     */
    public BigDecimal getBaseDeliveryFee() {
        return BASE_DELIVERY_FEE;
    }
}