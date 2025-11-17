package com.order.enums;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    PENDING("pending", "待处理"),
    PAID("paid", "已支付"),
    CONFIRMED("confirmed", "已确认"),
    REJECTED("rejected", "已拒绝"),
    PREPARING("preparing", "准备中"),
    DELIVERING("delivering", "配送中"),
    DELIVERED("delivered", "已送达"),
    CANCELLED("cancelled", "已取消"),
    COMPLETED("completed", "已完成");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status code: " + code);
    }
}