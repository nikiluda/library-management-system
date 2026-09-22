package com.zhanlin.library_management_system.security.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        ProblemDetail problemDetail = ProblemDetail.forStatus(ErrorCode.FORBIDDEN.getStatus());

        problemDetail.setTitle(ErrorCode.FORBIDDEN.getTitle());
        problemDetail.setDetail(ApiErrorMessage.FORBIDDEN.getMessage());

        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.FORBIDDEN.name());

        response.setStatus(ErrorCode.FORBIDDEN.getStatus().value());

        objectMapper.writeValue(response.getWriter(), problemDetail);
    }
}
