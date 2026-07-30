package com.zhanlin.library_management_system.logging.aspect;


import com.zhanlin.library_management_system.logging.annotation.Audit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger("AUDIT");

    @AfterReturning("@annotation(audit)")
    public void logAudit(JoinPoint joinPoint, Audit audit) {

        String method = joinPoint.getSignature().toShortString();


        log.info(
                "AUDIT | action={} | method={}",
                audit.value(),
               method
        );
    }
}
