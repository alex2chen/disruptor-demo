package com.disruptor.exam;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ScoreEventHandler implements EventHandler<ScoreEvent>, WorkHandler<ScoreEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreEventHandler.class);
    private Processor processor;

    public ScoreEventHandler(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void onEvent(ScoreEvent scoreEvent, long l, boolean b) throws Exception {
        onEvent(scoreEvent);
    }

    @Override
    public void onEvent(ScoreEvent scoreEvent) throws Exception {
        if (ScoreEvent.class.equals(processor.getClazz())) { //这里表示是计数器任务，直接运行
            LOGGER.debug("onEvent.ScoreEvent,{}", scoreEvent);
            processor.process(scoreEvent);
        } else {
            //如果报错了就不在往下执行
            if (scoreEvent.isHasError()) {
                return;
            }
            LOGGER.debug("onEvent.next,{}", scoreEvent.getProcessorContext());
            processor.process(scoreEvent.getProcessorContext()); //将事件中传递的数据对象往下传递执行
        }
    }
}
