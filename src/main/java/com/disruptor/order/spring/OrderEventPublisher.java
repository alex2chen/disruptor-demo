package com.disruptor.order.spring;

import com.disruptor.order.base.event.OrderEvent;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
@Component
public class OrderEventPublisher implements IEventPublisher<OrderEvent>, InitializingBean {
    private Disruptor<OrderEvent> disruptor;
    private static final EventTranslatorOneArg<OrderEvent, OrderEvent> translator = new
            EventTranslatorOneArg<OrderEvent, OrderEvent>() {
                public void translateTo(OrderEvent event, long sequence, OrderEvent arg0) {
                    // event = arg0;//为毛不能这个要深刻理解
                    event.setOrderId(arg0.getOrderId());
                }
            };

    public void publish(OrderEvent event) {
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.tryPublishEvent(translator, event);//发布事件；
    }

    public void afterPropertiesSet() throws Exception {
        disruptor = new Disruptor<OrderEvent>(new EventFactory<OrderEvent>() {
            public OrderEvent newInstance() {
                return new OrderEvent();
            }
        }, 1024, Executors.newFixedThreadPool(10), ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new OrderSubmitEventHandler());
        disruptor.start();
    }
}
