package com.xfboy.exam;

import com.disruptor.exam.Processor;
import com.disruptor.exam.ProcessorContext;
import com.disruptor.exam.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/29
 */
public class FillItemProcessor implements Processor<ProcessorContext> {
    private static final Logger logger = LoggerFactory.getLogger(FillItemProcessor.class);

    @Override
    public Class<ProcessorContext> getClazz() {
        return ProcessorContext.class;
    }

    @Override
    public void process(ProcessorContext data) {
        List<Student> students = data.getStudents();
        if (students != null && !students.isEmpty())
            logger.info(students.stream().map(Student::getName).collect(Collectors.joining()) + "开始做填空题...");
    }
}
