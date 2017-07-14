package com.xfboy.order;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler5 implements EventHandler<OrderEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Handler5.class);

    public void onEvent(OrderEvent order, long paramLong, boolean paramBoolean) throws Exception {
        LOGGER.debug("handler5 get price :" + order.getOrderFee());
        order.setOrderFee(order.getOrderFee() + 100);
        Thread.sleep(1000);

    }

}
