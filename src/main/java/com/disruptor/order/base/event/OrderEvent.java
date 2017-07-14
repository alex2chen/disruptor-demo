package com.disruptor.order.base.event;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
public class OrderEvent extends AbstractEvent {
    private String orderId;
    private String orderType;
    private double orderFee;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(double orderFee) {
        this.orderFee = orderFee;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId='" + orderId + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderFee=" + orderFee +
                '}';
    }
}
