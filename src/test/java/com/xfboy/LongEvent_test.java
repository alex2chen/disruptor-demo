package com.xfboy;

import com.disruptor.simple.*;
import com.google.common.base.Stopwatch;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class LongEvent_test {
    private static final Logger LOGGER = LoggerFactory.getLogger(LongEvent_test.class);

    @Test
    public void go_Long_PublishEvent() throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Disruptor disruptor = getDisruptor(false);
        // 发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();//请求下一个事件序号；
        try {
            LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
            event.setValue(123);
        } finally {
            /*
             注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；
             如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
             */
            ringBuffer.publish(sequence);//发布事件；
        }
        //关闭 Disruptor
        //disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
        //executor.shutdown();//关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭；
        disruptor.halt();
        LOGGER.debug("任务执行完毕,共耗时{}", stopwatch);
        //System.in.read();
    }

    @Test
    public void go_Long_PublishEvent2() throws IOException {
        Disruptor disruptor = getDisruptor(false);
        // 发布事件；
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        final LongEventProducerTranslator TRANSLATOR = new LongEventProducerTranslator(ringBuffer);
        /**
         * Disruptor要求RingBuffer.publishEvent 必须得到调用的潜台词就是，如果发生异常也一样要调用 publish ，
         * 那么，很显然这个时候需要调用者在事件处理的实现上来判断事件携带的数据是否是正确的或者完整的，这是实现者应该要注意的事情。
         */
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, 456L);
        ringBuffer.publishEvent(TRANSLATOR, buffer);
        disruptor.shutdown();
        System.in.read();
    }

    @Test
    public void go_LongProducer() throws IOException {
        Disruptor disruptor = getDisruptor(true);
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducerTranslator producer = new LongEventProducerTranslator(ringBuffer);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        for (int i = 0; i < 10; i++) {
            buffer.putLong(0, Long.valueOf(i + ""));
            producer.onData(buffer);
        }
        disruptor.shutdown();
        System.in.read();
    }

    private Disruptor getDisruptor(boolean enabledThreadFaction) {
        //实例化Disruptor
        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        int ringBufferSize = 1024 * 1024; // ringBufferSize 大小，必须是 2 的 N 次方；
        Disruptor<LongEvent> disruptor = null;
        if (enabledThreadFaction) {
            disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, new LongThreadFactory(),
                    ProducerType.SINGLE, new YieldingWaitStrategy());

        } else {
            disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, executor, ProducerType.SINGLE,
                    new YieldingWaitStrategy());
        }
        disruptor.handleEventsWith(new LongEventHandler());
        //启动
        disruptor.start();
        return disruptor;
    }
}
