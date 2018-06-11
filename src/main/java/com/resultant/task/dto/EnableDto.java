package com.resultant.task.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EnableDto {
    @NotNull
    private Long id;
    @NotNull
    private Boolean check;
}
