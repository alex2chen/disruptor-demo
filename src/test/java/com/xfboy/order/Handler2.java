package com.xfboy.order;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler2 implements EventHandler<OrderEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Handler2.class);
	public void onEvent(OrderEvent order, long paramLong, boolean paramBoolean) throws Exception {
		LOGGER.debug("hanlder2 set price");
		order.setOrderFee(200);
		Thread.sleep(1000);

	}

}
