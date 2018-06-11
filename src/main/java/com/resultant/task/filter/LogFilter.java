package com.resultant.task.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter extends GenericFilterBean {

    private final String logId;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put(logId, UUID.randomUUID().toString());
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            log.debug("type content:" + httpRequest.getHeader("Accept"));

            httpRequest.getParameterMap().forEach((key, value) ->{
                log.debug( key + " : " + httpRequest.getParameter(key));

            });



            chain.doFilter(request, response);

        }finally {
            MDC.clear();
        }
    }

    public LogFilter(String logId) {
        super();
        this.logId = logId;
    }
}
