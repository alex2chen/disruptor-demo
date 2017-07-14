package com.disruptor.exam;

/**
 * 响应处理
 * Created by Administrator on 2017/6/29.
 */
public interface Processor<T> {
    Class<T> getClazz();

    void process(T data);
}
