package com.disruptor.order.processor;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class OrderProducer implements EventTranslator<OrderEvent> {
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void translateTo(OrderEvent order, long l) {
        order.setOrderId("1");
        order.setOrderType("订单");
        order.setOrderFee(2);
    }

    public void onData() {
        ringBuffer.publishEvent(this);
    }
}
