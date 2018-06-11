package com.resultant.task.service;

import com.resultant.task.entity.Course;

import java.util.List;

public interface BatchService {

    Integer bulkWithEntityManager(List<Course> items);
}
