package com.xfboy.order;

import com.disruptor.order.base.event.OrderEvent;
import com.disruptor.order.base.event.OrderEventFactory;
import com.disruptor.order.processor.OrderProducer;
import com.disruptor.order.processor.OrderThreadFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import org.junit.Test;

import java.io.IOException;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class Order_manyHandle_test {
    private OrderEventFactory eventFactory = new OrderEventFactory();

    // handler1和Handler2并行消费，handler1和hanlder3消费完，再轮到handler3消费
    @Test
    public void go_ConcurrentRun() throws IOException {
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(eventFactory, Util.ceilingNextPowerOfTwo(1024), new OrderThreadFactory(),
                ProducerType.SINGLE, new YieldingWaitStrategy());
        EventHandlerGroup<OrderEvent> group = disruptor.handleEventsWith(new Handler1(), new Handler2());
        group.then(new Handler3());
        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderProducer producer = new OrderProducer(ringBuffer);
        producer.onData();
        System.in.read();
    }

    //按handler1，handler2，handler3顺序消费
    @Test
    public void go_SortRun() throws IOException {
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(eventFactory, Util.ceilingNextPowerOfTwo(1024), new OrderThreadFactory(),
                ProducerType.SINGLE, new YieldingWaitStrategy());
        disruptor.handleEventsWith(new Handler1());
        disruptor.handleEventsWith(new Handler2());
        disruptor.handleEventsWith(new Handler3());
        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderProducer producer = new OrderProducer(ringBuffer);
        producer.onData();
        System.in.read();
    }

    // 六边形消费
    // handler1,handler2并行消费，handler1消费后，轮到handler4消费，hangler2消费完轮到handler5消费，hangler4和handler5消费完，最后轮到handler3消费
    @Test
    public void go_SexangleRun() {
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(eventFactory, Util.ceilingNextPowerOfTwo(1024 * 1024), new OrderThreadFactory(),
                ProducerType.SINGLE, new YieldingWaitStrategy());
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        Handler3 handler3 = new Handler3();
        Handler4 handler4 = new Handler4();
        Handler5 handler5 = new Handler5();
        disruptor.handleEventsWith(handler1, handler2);
        disruptor.after(handler1).handleEventsWith(handler4);
        disruptor.after(handler2).handleEventsWith(handler5);
        disruptor.after(handler4, handler5).handleEventsWith(handler3);
        disruptor.start();

        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderProducer producer = new OrderProducer(ringBuffer);
        producer.onData();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        disruptor.shutdown();
    }
}
