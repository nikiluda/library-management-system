package com.zhanlin.library_management_system.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class PerformanceLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(PerformanceLoggingAspect.class);


    @Around(
            "@annotation(com.zhanlin.library_management_system.logging.annotation.LogExecutionTime)"
    )
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startedAt = System.nanoTime();
        String outcome = "FAILURE";

        try {
            Object result = joinPoint.proceed();
            outcome = "SUCCESS";
            return result;
        } finally {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);

            log.atInfo()
                    .addKeyValue("event", "service_performance")
                    .addKeyValue("operation", joinPoint.getSignature().toShortString())
                    .addKeyValue("outcome", outcome)
                    .addKeyValue("durationMs", durationMs)
                    .log("Service operation completed");

        }
    }

}
