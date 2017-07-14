package com.disruptor.simple;

import java.util.concurrent.ThreadFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class LongThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
