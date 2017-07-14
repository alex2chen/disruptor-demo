package com.disruptor.exam;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * 一般任务工作组
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ProcessorGroup {
    private ImmutableList<Processor> processors;

    public ImmutableList<Processor> getProcessors() {
        return processors;
    }

    public ProcessorGroup setProcessors(ImmutableList<Processor> processors) {
        Preconditions.checkNotNull(processors, "数据处理不能空");
        this.processors = ImmutableList.copyOf(processors);
        return this;
    }

    public ProcessorGroup setProcessors(Processor... processors) {
        Preconditions.checkNotNull(processors, "数据处理不能空");
        this.processors = ImmutableList.copyOf(processors);
        return this;
    }
}
