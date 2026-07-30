package com.zhanlin.library_management_system.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);


    @Around("execution(* com.zhanlin.library_management_system.services..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getTarget().getClass().getSimpleName();

        String methodName = joinPoint.getSignature().getName();

        String operation = className + "." + methodName + "()";

        log.debug("Entering {}", operation);

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;

            log.info(
                    "Completed {} in {} ms",
                    operation,
                    duration

            );

            return result;

        } finally {

            log.debug("Leaving {}", operation);
        }

    }

}
