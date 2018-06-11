package com.resultant.task.dao;

import com.resultant.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User getByUsername(String username);
    User getById(Long id);


}
