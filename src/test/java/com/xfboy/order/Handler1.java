package com.xfboy.order;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler1 implements EventHandler<OrderEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Handler1.class);
	public void onEvent(OrderEvent order, long paramLong, boolean paramBoolean) throws Exception {
		LOGGER.debug("handler1 set name");
		order.setOrderType("order-h1");
		Thread.sleep(1000);
	}

}
