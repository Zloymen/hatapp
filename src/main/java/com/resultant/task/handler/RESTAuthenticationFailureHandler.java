package com.resultant.task.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Zloy on 29.08.2017.
 */

@PropertySource("classpath:appenv.properties")
public class RESTAuthenticationFailureHandler extends AbstractAccessHandler implements AuthenticationFailureHandler {

    @Qualifier("serializingObjectMapper")
    @Autowired
    private ObjectMapper mapper;

    @Value("${template.log-id}")
    private String logId;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        onFailure(mapper, logId, response, exception);
    }
}