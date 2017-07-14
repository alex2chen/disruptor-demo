package com.disruptor.order.processor;

import java.util.concurrent.ThreadFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class OrderThreadFactory implements ThreadFactory {

    public Thread newThread(Runnable r) {
        // TODO Auto-generated method stub
        return new Thread(r);
    }

}
