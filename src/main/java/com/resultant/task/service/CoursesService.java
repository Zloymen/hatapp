package com.resultant.task.service;

import com.resultant.task.entity.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CoursesService {

    List<Course> getByCharCodeAndDate(List<String> charCodes, LocalDate date);
    void upload(LocalDate date);

    List<Map<String, String>> getDynamicCourses(LocalDate from, LocalDate to, List<String> charCodes);
}
