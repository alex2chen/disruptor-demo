package com.disruptor.exam;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public abstract class TaskContainer<T> {
    protected int index = 0;
    protected T current = null;
    protected List<T> tasks = Lists.newArrayList();

    public TaskContainer(List<T> tasks) {
        this.tasks = tasks;
    }
    public boolean hasNext() {
        try {
            current = tasks.get(index++);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public T next() {
        return current;
    }
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * 每个任务执行中需要传递的实际数据对象
     * 该对象由当前任务中的cur和不同任务需要的其他数据封装而成
     *
     * @return
     */
    public abstract Object get();
}
