package com.disruptor.simple;

/**
 * 定义事件
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class LongEvent {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}
