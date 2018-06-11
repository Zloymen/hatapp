package com.resultant.task.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PasswordDto {

    @NotEmpty
    private String newPass;

    @NotEmpty
    private String oldPass;
}
