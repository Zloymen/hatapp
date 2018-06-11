package com.resultant.task.service;

import com.resultant.task.entity.Course;

import java.util.List;

public interface AsyncService {

    void saveCources(List<Course> list);
}
