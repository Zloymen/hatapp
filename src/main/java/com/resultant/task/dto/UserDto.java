package com.resultant.task.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDto {

    @NotEmpty @Email
    private String username;

    @Size(min=6, max = 124)
    private String password;

    @NotNull
    private List<RoleDto> roles;

}
