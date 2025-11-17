package com.order.enums;

/**
 * 支付状态枚举
 */
public enum PaymentStatus {
    UNPAID("unpaid", "未支付"),
    PAID("paid", "已支付"),
    REFUNDED("refunded", "已退款"),
    PARTIAL_REFUND("partial_refund", "部分退款");

    private final String code;
    private final String description;

    PaymentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown payment status code: " + code);
    }
}