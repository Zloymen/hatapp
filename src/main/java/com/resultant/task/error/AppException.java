package com.resultant.task.error;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class AppException extends RuntimeException {

    @Getter
    private final String text;

}
