package com.disruptor.exam;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务执行器
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class Performer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Performer.class);
    private List<ProcessorGroup> processorGroups = Lists.newArrayList();
    private AtomicBoolean hasError = new AtomicBoolean(false);
    private AtomicBoolean hasWarn = new AtomicBoolean(false);
    private Disruptor<ScoreEvent> disruptor;
    private TaskContainer task;
    private String pid = UUID.randomUUID().toString();

    public Performer(TaskContainer task) {
        this.task = task;
    }

    public String getPid() {
        return pid;
    }

    public Performer setTask(TaskContainer task) {
        this.task = task;
        return this;
    }

    public AtomicBoolean getHasError() {
        return hasError;
    }

    /**
     * 通过handleEvents处理
     *
     * @param processors
     * @return
     */
    public Performer addProcessors(Processor... processors) {
        processorGroups.add(new ProcessorGroup().setProcessors(processors));
        return this;
    }

    /**
     * 通过handleEventsWithWorkerPool处理
     *
     * @param processors
     * @return
     */
    public Performer addProcessorPool(Processor... processors) {
        processorGroups.add(new ProcessorGroupPool().setProcessors(processors));
        return this;
    }

    public void process() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            disruptor = new Disruptor<ScoreEvent>(new ScoreEventFactory(), Util.ceilingNextPowerOfTwo(1024),
                    ThreadExecutor.createThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
            disruptor.setDefaultExceptionHandler(new ScoreExceptionHandler(this));
            //创建计数任务
            CountDownLatch latch = new CountDownLatch(task.getTaskCount());
            //设置处理器
            EventHandlerGroup<ScoreEvent> handlerGroup = createEventHandlerGroup();
            createTaskCountHandler(handlerGroup, latch);
            LOGGER.debug("任务创建完成");
            //启动disruptor,准备接受任务
            disruptor.start();
            int index = 1;//当前任务索引
            while (task.hasNext()) {
                ScoreEvent event = new ScoreEvent();
                event.setMessage("");
                event.setProcessorContext(task.get());
                event.setIndex(index++);
                disruptor.publishEvent(new ScoreEventTranslator(event));
            }
            //等待任务执行完成之后再执行后续操作
            latch.await();

        } catch (Exception ex) {
            LOGGER.error("发送错误，", ex);
        }
        LOGGER.debug("任务执行完毕,共耗时{}", stopwatch);
    }

    /**
     * 处理任务
     *
     * @return 返回任务处理分组，主要是为了后续在任务当前任务组中添加计数器任务
     */
    private EventHandlerGroup<ScoreEvent> createEventHandlerGroup() {
        EventHandlerGroup<ScoreEvent> handlerGroup = null;
        ScoreEventHandler[] scoreEventHandlers = null;
        for (ProcessorGroup group : processorGroups) {
            scoreEventHandlers = toArray(group.getProcessors());
            if (group instanceof ProcessorGroupPool) {
                if (handlerGroup == null) {
                    handlerGroup = disruptor.handleEventsWithWorkerPool(scoreEventHandlers);
                } else {
                    handlerGroup = handlerGroup.handleEventsWithWorkerPool(scoreEventHandlers);
                }
            } else {
                if (handlerGroup == null) {
                    handlerGroup = disruptor.handleEventsWith(scoreEventHandlers);
                } else {
                    handlerGroup = handlerGroup.handleEventsWith(scoreEventHandlers);
                }
            }
        }
        return handlerGroup;
    }

    private ScoreEventHandler[] toArray(List<Processor> processors) {
        List<ScoreEventHandler> handlers = Lists.newArrayList();
        for (Processor processor : processors) {
            handlers.add(new ScoreEventHandler(processor));
        }
        return handlers.toArray(new ScoreEventHandler[processors.size()]);
    }

    /**
     * 创建计数器任务，用于计数当前执行的任务数
     *
     * @param handlerGroup
     */
    private void createTaskCountHandler(EventHandlerGroup<ScoreEvent> handlerGroup, final CountDownLatch latch) {
        ScoreEventHandler handler = new ScoreEventHandler(new Processor<ScoreEvent>() {
            public Class getClazz() {
                return ScoreEvent.class; //计数器任务标识
            }

            public void process(ScoreEvent event) {
                //这里-1是countDown在后面操作导致
                //这里开始更新进度条
                Progressor progressor = (Progressor) MemoryCache.getInstance().get(pid);
                if (progressor == null) {
                    return;
                }
                progressor.setComplated(progressor.getComplated() + 1);
                if (event.isHasError()) {
                    hasError.compareAndSet(false, true);
                    progressor.setHasError(true);
                    progressor.getErrorMessages().add(event.getMessage());
                }

                if (event.isHasWarn()) {
                    hasWarn.compareAndSet(false, true);
                    progressor.setHasWarn(true);
                    progressor.getWarnMessages().add(event.getMessage());
                }
                MemoryCache.getInstance().put(pid, new Progressor(progressor.getComplated(), progressor.getTotal(), progressor.getText()));
                latch.countDown();
                LOGGER.info("当前已经完成{},还有{}个任务...........", event.getIndex(), latch.getCount());
            }
        });

        if (handlerGroup != null) {
            handlerGroup.handleEventsWith(handler);
        } else {
            disruptor.handleEventsWith(handler);
        }
    }

}
