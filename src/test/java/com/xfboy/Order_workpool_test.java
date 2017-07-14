package com.xfboy;

import com.disruptor.order.base.event.OrderEvent;
import com.disruptor.order.base.event.OrderEventFactory;
import com.disruptor.order.processor.OrderProducer;
import com.lmax.disruptor.*;
import com.xfboy.order.OrderWorker2Handler;
import com.xfboy.order.OrderWorkerHandler;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class Order_workpool_test {

    @Test
    public void go_workpool() {
        RingBuffer<OrderEvent> ringBuffer = RingBuffer.createSingleProducer(new OrderEventFactory(), 1024 * 1024);
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        WorkHandler<OrderEvent>[] workHandlers = new WorkHandler[2];
        workHandlers[0] = new OrderWorkerHandler();
        workHandlers[1] = new OrderWorker2Handler();
        WorkerPool<OrderEvent> workerPool = new WorkerPool<OrderEvent>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), workHandlers);
        // 这一步的目的就是把消费者的位置信息引用注入到生产者 如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        ExecutorService executor = Executors.newFixedThreadPool(10);
        workerPool.start(executor);

        OrderProducer orderProducer = new OrderProducer(ringBuffer);
        orderProducer.onData();
        orderProducer.onData();
        orderProducer.onData();
        orderProducer.onData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 通知事件(或者说消息)处理器 可以结束了（并不是马上结束!!!）
        workerPool.halt();
        executor.shutdown();
    }
}
