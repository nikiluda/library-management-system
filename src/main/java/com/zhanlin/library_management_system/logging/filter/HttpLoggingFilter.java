package com.zhanlin.library_management_system.logging.filter;

import com.zhanlin.library_management_system.logging.LoggingConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        logRequest(request);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = (System.currentTimeMillis() - startTime);
            logResponse(request, response, duration);
        }

    }

    private void logRequest(HttpServletRequest request) {

        log.info(
                "Incoming HTTP request: method={}, url={}, query={}, client={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() == null ? "-" : request.getQueryString(),
                request.getRemoteAddr()

        );
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response,
                             long duration) {

        log.info(
                "Completed HTTP request: method={}, url={}, status={}, duration={} ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration
        );

    }
}
