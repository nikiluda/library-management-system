package com.zhanlin.library_management_system.security.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ProblemDetail problemDetail = ProblemDetail.forStatus(ErrorCode.UNAUTHORIZED.getStatus());

        problemDetail.setTitle(ErrorCode.UNAUTHORIZED.getTitle());
        problemDetail.setDetail(ApiErrorMessage.UNAUTHORIZED.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.UNAUTHORIZED.name());

        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
        response.setContentType("application/problem+json");

        objectMapper.writeValue(response.getWriter(), problemDetail);

    }
}
