package com.disruptor.order.base.handle;

import com.disruptor.order.base.event.AbstractEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
public abstract class AbstractEventHandler implements EventHandler<AbstractEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventHandler.class);

    public void onEvent(AbstractEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (preBusiness(event) && doCondition(event)) {
            onEvent(event);
        }
    }

    private void onEvent(AbstractEvent event) {
        LOGGER.debug("成功消费," + event);
    }

    protected abstract boolean doCondition(AbstractEvent event);

    protected abstract boolean preBusiness(AbstractEvent event);
}
