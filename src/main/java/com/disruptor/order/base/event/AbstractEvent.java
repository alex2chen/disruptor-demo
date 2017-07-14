package com.disruptor.order.base.event;

import java.io.Serializable;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
public abstract class AbstractEvent implements Serializable {
    private int source;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

}
