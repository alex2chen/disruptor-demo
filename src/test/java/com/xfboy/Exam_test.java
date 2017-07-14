package com.xfboy;

import com.disruptor.exam.MemoryCache;
import com.disruptor.exam.Performer;
import com.disruptor.exam.Progressor;
import com.disruptor.exam.Student;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmax.disruptor.util.Util;
import com.xfboy.exam.ChooseItemProcessor;
import com.xfboy.exam.ExamTask;
import com.xfboy.exam.FillItemProcessor;
import com.xfboy.exam.ZgItemProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class Exam_test {
    private Logger logger = LoggerFactory.getLogger(Exam_test.class);

    private List<Student> students;

    @Before
    public void initData() {
        students = Lists.newArrayList();
        students.add(new Student("alex"));
        students.add(new Student("lisa"));
    }

    @Test
    public void go_MemoryCache() throws Exception {
        final ConcurrentMap map = Maps.newConcurrentMap();
        final CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                public void run() {
                    map.put(MemoryCache.getInstance(), finalI);
                    latch.countDown();
                }
            }).start();
        }

        latch.await();

        System.out.println(map.keySet().size());
        for (Object key : map.keySet()) {
            System.out.println(key + "：" + map.get(key));
        }
    }

    @Test
    public void go_ceilingNextPowerOfTwo() {
        Assert.assertTrue(Util.ceilingNextPowerOfTwo(3) % 2 == 0);
        Assert.assertTrue(Util.ceilingNextPowerOfTwo(1024) % 2 == 0);
    }

    @Test
    public void go_handleEventsWith() {
        ExamTask task = new ExamTask(students);
        //Choose>Fill>Zg>Choose>ScoreEvent
        Performer performer = new Performer(task);
        performer.addProcessors(new ChooseItemProcessor())
                .addProcessors(new FillItemProcessor())
                .addProcessors(new ZgItemProcessor(), new ChooseItemProcessor());
        MemoryCache.getInstance().put(performer.getPid(), new Progressor(0, task.getTaskCount(), "准备执行任务..."));
        performer.process();
        //初始化进度条
        logger.info("考试结束...");
    }

    @Test
    public void go_handleEventsWithWorkerPool() {
        ExamTask task = new ExamTask(students);
        Performer performer = new Performer(task);
        performer.addProcessorPool(new ChooseItemProcessor(), new ChooseItemProcessor())
                .addProcessorPool(new FillItemProcessor(), new ChooseItemProcessor())
                .addProcessorPool(new ZgItemProcessor(), new ChooseItemProcessor());
        MemoryCache.getInstance().put(performer.getPid(), new Progressor(0, task.getTaskCount(), "准备执行任务..."));
        performer.process();
        Progressor progressor = (Progressor) MemoryCache.getInstance().get(performer.getPid());
        System.out.println(progressor.getTotal() + "," + progressor.getComplated() + "," + progressor.isFinished());
        //初始化进度条
        logger.info("考试结束...");
    }
}
