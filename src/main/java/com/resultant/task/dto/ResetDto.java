package com.resultant.task.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ResetDto {

    @Email @NotEmpty
    private String username;
}
