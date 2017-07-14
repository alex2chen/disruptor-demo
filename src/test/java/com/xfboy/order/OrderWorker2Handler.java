package com.xfboy.order;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class OrderWorker2Handler implements WorkHandler<OrderEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderWorker2Handler.class);

    @Override
    public void onEvent(OrderEvent order) throws Exception {
        LOGGER.debug("成功消费：" + order);
    }
}
