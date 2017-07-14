package com.disruptor.exam;

import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ScoreEvent {
    private Object processorContext; //传递的数据对象
    private String message; //在任务传递过程中的一些信息，比如错误信息，警告信息等
    private boolean hasError = Boolean.FALSE; // 是否在执行过程中有错误
    private boolean hasWarn = Boolean.FALSE; //是否在执行过程中有警告信息
    private int index; //当前执行的任务索引号

    public void copy(ScoreEvent from) {
        this.processorContext = from.processorContext;
        this.index = from.index;
        this.hasError = false;
        this.hasWarn = false;
        message = "";
    }

    public Object getProcessorContext() {
        return processorContext;
    }

    public void setProcessorContext(Object processorContext) {
        this.processorContext = processorContext;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasWarn() {
        return hasWarn;
    }

    public void setHasWarn(boolean hasWarn) {
        this.hasWarn = hasWarn;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ScoreEvent{" +
                "processorContext=" + processorContext +
                ", message='" + message + '\'' +
                ", hasError=" + hasError +
                ", hasWarn=" + hasWarn +
                ", index=" + index +
                '}';
    }
}
