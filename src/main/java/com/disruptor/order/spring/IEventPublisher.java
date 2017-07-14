package com.disruptor.order.spring;

import com.disruptor.order.base.event.AbstractEvent;

/**
 * Created by Administrator on 2017/6/27.
 */
public interface IEventPublisher<T extends AbstractEvent> {
    void publish(T event);
}
