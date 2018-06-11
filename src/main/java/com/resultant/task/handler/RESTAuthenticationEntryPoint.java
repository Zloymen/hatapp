package com.resultant.task.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Zloy on 29.08.2017.
 */

public class RESTAuthenticationEntryPoint extends AbstractAccessHandler implements AuthenticationEntryPoint {

    @Qualifier("serializingObjectMapper")
    @Autowired
    private ObjectMapper mapper;

    @Value("${template.log-id}")
    private String logId;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        if(authException instanceof InsufficientAuthenticationException){

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else  onFailure(mapper, logId, response, authException);

    }
}