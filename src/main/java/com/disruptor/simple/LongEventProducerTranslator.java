package com.disruptor.simple;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class LongEventProducerTranslator implements EventTranslatorOneArg<LongEvent, ByteBuffer> {
    private RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void translateTo(LongEvent longEvent, long l, ByteBuffer data) {
        longEvent.setValue(data.getLong(0));
    }
    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(this, bb);
    }
}
