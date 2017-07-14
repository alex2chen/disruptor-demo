package com.disruptor.exam;

import com.lmax.disruptor.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ScoreExceptionHandler implements ExceptionHandler<ScoreEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreExceptionHandler.class);
    private Performer performer;

    public ScoreExceptionHandler(Performer performer) {
        this.performer = performer;
    }

    public void handleEventException(Throwable throwable, long l, ScoreEvent event) {
        performer.getHasError().compareAndSet(false, true);
        String message = MessageFormat.format("处理{0}数据出错", event.getProcessorContext().toString());
        LOGGER.error("错误,cause:" + throwable.getMessage(), throwable);
        event.setHasError(true);
        event.setMessage(message);
    }

    public void handleOnStartException(Throwable throwable) {
        LOGGER.error("启动disruptor出错,cause:" + throwable.getMessage(), throwable);
    }

    public void handleOnShutdownException(Throwable throwable) {
        LOGGER.error("关闭disruptor出错,cause:" + throwable.getMessage(), throwable);
    }
}
