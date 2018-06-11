package com.resultant.task.service.impl;

import com.resultant.task.entity.Course;
import com.resultant.task.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchServiceImpl implements BatchService {

    private final EntityManagerFactory emf;

    @Override
    public Integer bulkWithEntityManager(List<Course> items) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        items.forEach(entityManager::persist);
        entityManager.getTransaction().commit();
        entityManager.close();

        return items.size();
    }
}