package com.disruptor.exam;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ThreadExecutor {
    private static AtomicInteger poolNo = new AtomicInteger(0);

    public static ThreadFactory createThreadFactory() {
        ThreadFactory factory = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                SecurityManager manager = System.getSecurityManager();
                ThreadGroup group = manager != null ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread thread = new Thread(group, r, "Thread-disruptor-" + poolNo.getAndIncrement() + "-");
                if (thread.isDaemon()) {
                    thread.setDaemon(Boolean.FALSE);
                }
                if (thread.getPriority() != Thread.NORM_PRIORITY) {
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        };

        return factory;
    }
}
