package com.resultant.task.service;

import com.resultant.task.dto.EnableDto;
import com.resultant.task.dto.PasswordDto;
import com.resultant.task.dto.ResetDto;
import com.resultant.task.dto.UserDto;
import com.resultant.task.entity.User;
import com.resultant.task.entity.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {

    void create(UserDto dto);

    User getCurrentUser();

    User updatePassword(PasswordDto dto);

    void resetPassword(ResetDto dto);

    Collection<User> getAllUser();

    User enabledUser(EnableDto dto);

    List<UserRole> getRoles();

    User getUserById(Long id);

    User replacePassword(EnableDto dto);

}
