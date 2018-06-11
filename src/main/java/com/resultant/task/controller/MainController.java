package com.resultant.task.controller;


import com.resultant.task.dto.*;
import com.resultant.task.entity.User;
import com.resultant.task.entity.UserRole;
import com.resultant.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity registration(@RequestBody @Valid UserDto user) {
        userService.create(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @ResponseBody
    public User getCurrent() {
        return userService.getCurrentUser();
    }

    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    @ResponseBody
    public User updatePassword(@RequestBody @Valid ParamDto<PasswordDto> dto) {
        return userService.updatePassword(dto.getData());
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity resetPassword(@RequestBody @Valid ResetDto dto) {
        userService.resetPassword(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Collection<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/replace/enabled", method = RequestMethod.POST)
    @ResponseBody
    public User enableUser(@RequestBody @Valid EnableDto dto) {
        return userService.enabledUser(dto);
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/replace/password", method = RequestMethod.POST)
    @ResponseBody
    public User replacePassword(@RequestBody @Valid EnableDto dto) {
        return userService.replacePassword(dto);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public Collection<UserRole> getAllRoles() {
        return userService.getRoles();
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
