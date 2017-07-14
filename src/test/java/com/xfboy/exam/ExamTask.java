package com.xfboy.exam;

import com.disruptor.exam.ProcessorContext;
import com.disruptor.exam.Student;
import com.disruptor.exam.TaskContainer;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class ExamTask extends TaskContainer<Student> {
    public ExamTask(List<Student> students) {
        super(students);
    }

    @Override
    public Object get() {
        return new ProcessorContext().addStudent(current);
    }
}
