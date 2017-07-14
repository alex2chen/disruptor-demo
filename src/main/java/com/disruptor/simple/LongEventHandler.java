package com.disruptor.simple;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件处理的具体实现
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LongEventHandler.class);

    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        LOGGER.debug("完成消费,Event: " + event);
        // throw new RuntimeException("error");
    }
}
