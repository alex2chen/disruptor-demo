package com.disruptor.simple;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class LongEventProducer {
    private RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * onData用来发布事件，每调用一次就发布一次事件 它的参数会用过事件传递给消费者
     */
    public void onData(ByteBuffer bb) {
        // 1.可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
        long sequence = ringBuffer.next();
        try {
            // 2.用上面的索引取出一个空的事件用于填充（获取该序号对应的事件对象）
            LongEvent event = ringBuffer.get(sequence);
            // 3.获取要通过事件传递的业务数据
            event.setValue(bb.getLong(0));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // 4.发布事件
            // 注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；如果某个请求的 sequence 未被提交
            ringBuffer.publish(sequence);
        }

    }
}
