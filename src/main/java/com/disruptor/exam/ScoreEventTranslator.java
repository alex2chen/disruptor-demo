package com.disruptor.exam;

import com.lmax.disruptor.EventTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ScoreEventTranslator implements EventTranslator<ScoreEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreEventTranslator.class);
    private ScoreEvent event;

    public ScoreEventTranslator(ScoreEvent event) {
        this.event = event;
    }

    @Override
    public void translateTo(ScoreEvent scoreEvent, long l) {
        LOGGER.info("translateTo" + scoreEvent);
        scoreEvent.copy(event);
    }
}
