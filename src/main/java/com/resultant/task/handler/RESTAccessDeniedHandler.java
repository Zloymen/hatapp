package com.resultant.task.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Zloy on 29.08.2017.
 */

public class RESTAccessDeniedHandler extends AbstractAccessHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper mapper;

    @Value("${template.log-id}")
    private String logId;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        onFailure(mapper, logId, httpServletResponse, e);
    }
}