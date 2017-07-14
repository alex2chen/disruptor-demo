package com.disruptor.exam;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/7/1
 */
public class ProcessorContext implements Serializable {
    private List<Student> students;

    public ProcessorContext() {
        students = Lists.newArrayList();
    }

    public ProcessorContext(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public ProcessorContext addStudent(Student student) {
        students.add(student);
        return this;
    }

    @Override
    public String toString() {
        return "ProcessorContext{" +
                "students=" + students +
                '}';
    }
}
