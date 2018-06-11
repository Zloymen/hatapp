package com.resultant.task.service.impl;

import com.resultant.task.service.CoursesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private final CoursesService coursesService;

    @Scheduled(cron="0 0 10 ? * MON-FRI")
    public void uploadData(){
        coursesService.upload(LocalDate.now());
    }
}
