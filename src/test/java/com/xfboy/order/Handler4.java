package com.xfboy.order;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler4 implements EventHandler<OrderEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Handler4.class);
	public void onEvent(OrderEvent order, long paramLong, boolean paramBoolean) throws Exception {
		LOGGER.debug("handler4 get name :" + order.getOrderType());
		order.setOrderType(order.getOrderType() + "-h4");
		Thread.sleep(1000);

	}

}
