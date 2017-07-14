package com.disruptor.order.spring;


import com.disruptor.order.base.event.AbstractEvent;
import com.disruptor.order.base.handle.AbstractEventHandler;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
public class OrderSubmitEventHandler<OrderEvent> extends AbstractEventHandler {

    protected boolean doCondition(AbstractEvent event) {
        return true;
    }

    protected boolean preBusiness(AbstractEvent event) {
        return true;
    }
}