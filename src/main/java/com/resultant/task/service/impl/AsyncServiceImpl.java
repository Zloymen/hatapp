package com.resultant.task.service.impl;

import com.resultant.task.entity.Course;
import com.resultant.task.service.AsyncService;
import com.resultant.task.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {

    private final BatchService batchService;

    @Async
    @Override
    public void saveCources(List<Course> list)  {
        batchService.bulkWithEntityManager(list);
    }
}
