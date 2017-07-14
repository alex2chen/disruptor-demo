package com.disruptor.exam;

import com.lmax.disruptor.EventFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ScoreEventFactory implements EventFactory<ScoreEvent> {
    @Override
    public ScoreEvent newInstance() {
        return new ScoreEvent();
    }
}
