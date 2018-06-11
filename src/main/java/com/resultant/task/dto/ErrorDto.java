package com.resultant.task.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDto {
    @JsonProperty
    private String uuid;
    @JsonProperty
    private String message;

    public ErrorDto(String uuid, String message){
        this.uuid = uuid;
        this.message = message;
    }

}
