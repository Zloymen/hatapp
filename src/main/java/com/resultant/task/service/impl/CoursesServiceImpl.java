package com.resultant.task.service.impl;

import com.resultant.task.annotation.ReadOnlyTransactional;
import com.resultant.task.connector.CBRClient;
import com.resultant.task.dao.CourseDao;
import com.resultant.task.entity.Course;
import com.resultant.task.error.AppException;
import com.resultant.task.service.AsyncService;
import com.resultant.task.service.CoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CoursesServiceImpl implements CoursesService {

    private final CourseDao courseDao;

    private final CBRClient client;

    private final AsyncService asyncService;

    @ReadOnlyTransactional
    @Override
    public List<Course> getByCharCodeAndDate(List<String> charCodes, LocalDate date) {

        List<Course> courses = courseDao.getByCharCodeAndDate(charCodes, date);

        if(courses == null) throw new AppException("Bad work CBR service");

        if (courses.isEmpty()){
            List<Map<String, String>> list = client.getCursOnDate(date);

            List<Course> courseList = toCourses(list, date);
            asyncService.saveCources(courseList);
            courses = courseList.stream().filter(item -> charCodes.contains(item.getChCode())).collect(Collectors.toList());
        }

        return courses;
    }

    @ReadOnlyTransactional
    @Override
    public void upload(LocalDate date) {
        List<Course> courses = courseDao.getAllByDay(date);
        if(courses == null) throw new AppException("Bad work CBR service");
        if(courses.isEmpty()){
            List<Map<String, String>> list = client.getCursOnDate(date);
            List<Course> courseList = toCourses(list, date);

            asyncService.saveCources(courseList);
        }
    }

    @Override
    public List<Map<String, String>> getDynamicCourses(LocalDate from, LocalDate to, List<String> charCodes) {
        List<Map<String, String>> dynamicCourses = new ArrayList<>();
        charCodes.forEach(item ->{
            List<Map<String, String>> dynamicCoursesByCode = client.getCursDynamic(from, to, item);
            dynamicCourses.addAll(dynamicCoursesByCode);
        });
        return dynamicCourses;
    }

    private List<Course> toCourses(List<Map<String, String>> list , LocalDate date){
        List<Course> courseList = new ArrayList<>();
        for(Map<String, String> item : list){
            Course course = Course.create(item);
            course.setDay(date);
            courseList.add(course);
        }
        return courseList;
    }
}
