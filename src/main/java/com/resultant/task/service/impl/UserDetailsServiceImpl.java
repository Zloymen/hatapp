package com.resultant.task.service.impl;

import com.resultant.task.annotation.ReadOnlyTransactional;
import com.resultant.task.dao.RoleDao;
import com.resultant.task.dao.UserDao;
import com.resultant.task.dto.*;
import com.resultant.task.entity.User;
import com.resultant.task.entity.UserRole;
import com.resultant.task.error.AppException;
import com.resultant.task.service.MailSenderService;
import com.resultant.task.service.UserService;
import com.resultant.task.util.PassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("AppDetailsService")
@PropertySource("classpath:appenv.properties")
public class UserDetailsServiceImpl implements UserService {

    @Value("${template.password_patern}")
    private String patern;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ReadOnlyTransactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Transactional
    @Override
    public void create(UserDto dto) {
        List<UserRole> role = roleDao.findAllByIds(dto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toList()));
        userDao.save(new User(dto, passwordEncoder, role));
    }

    @ReadOnlyTransactional
    @Override
    public User getCurrentUser(){
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || a instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = a.getPrincipal();
        if (principal instanceof String &&  principal.equals("anonymousUser")) {
            return null;
        }

        return principal instanceof String ? userDao.getByUsername((String) principal) : userDao.getById((((User) principal).getId()));
    }

    @Transactional
    @Override
    public User updatePassword(PasswordDto dto) {
        User user = getCurrentUser();
        if(user == null) throw new AppException("User not found");
        if(!passwordEncoder.matches( dto.getOldPass(), user.getPassword())) throw new AppException("Wrong old password");
        if (!dto.getNewPass().matches(patern)) throw new AppException("Not a strong password");
        user.setPassword(passwordEncoder.encode(dto.getNewPass()));
        user.setReplacePass(false);
        return userDao.save(user);
    }

    @Transactional
    @Override
    public void resetPassword(ResetDto dto) {

        User user = userDao.getByUsername(dto.getUsername());

        if(user == null) throw new AppException("User not found");

        String newPass = PassUtil.generateRandomString(6, 10, 1, 1, 1, 0);

        mailSenderService.sendResetPassword(user, newPass);

        user.setReplacePass(true);
        user.setPassword(passwordEncoder.encode(newPass));

        userDao.save(user);

    }

    @ReadOnlyTransactional
    @Override
    public Collection<User> getAllUser() {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public User enabledUser(EnableDto dto) {

        User user = userDao.getById(dto.getId());
        user.setEnabled(dto.getCheck());

        return userDao.save(user);
    }

    @Transactional
    @Override
    public List<UserRole> getRoles() {
        return roleDao.findAll();
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        return userDao.getById(id);
    }

    @Transactional
    @Override
    public User replacePassword(EnableDto dto) {
        User user = userDao.getById(dto.getId());
        user.setReplacePass(dto.getCheck());

        return userDao.save(user);
    }
}