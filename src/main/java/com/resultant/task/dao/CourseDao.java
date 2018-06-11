package com.resultant.task.dao;

import com.resultant.task.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CourseDao  extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.chCode in :codes and c.day = :date")
    List<Course> getByCharCodeAndDate(@Param("codes") List<String> charCodes, @Param("date") LocalDate date);

    List<Course> getAllByDay(LocalDate date);
}
