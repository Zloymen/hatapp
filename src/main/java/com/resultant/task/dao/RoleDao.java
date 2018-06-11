package com.resultant.task.dao;

import com.resultant.task.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleDao extends JpaRepository<UserRole, Long> {

    UserRole findByName(String name);
    @Query("select r from UserRole r where r.id in :list")
    List<UserRole> findAllByIds(@Param("list") List<Integer> list);
}
