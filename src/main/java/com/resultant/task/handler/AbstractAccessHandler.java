package com.resultant.task.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resultant.task.dto.ErrorDto;
import org.slf4j.MDC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractAccessHandler {

    protected void onFailure(ObjectMapper mapper, String logId, HttpServletResponse response, Exception exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        PrintWriter out = response.getWriter();
        String uuid = MDC.get(logId);
        ErrorDto dto = new ErrorDto(uuid, exception.getMessage());
        mapper.writeValue(out, dto);
    }
}
