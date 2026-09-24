package com.zhanlin.library_management_system.logging.aspect;


import com.zhanlin.library_management_system.logging.annotation.Audit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger("AUDIT");

    @AfterReturning("@annotation(audit)")
    public void logAudit(JoinPoint joinPoint, Audit audit) {
        log.atInfo()
                .addKeyValue("event", "business_audit")
                .addKeyValue("action", audit.value().name())
                .addKeyValue("outcome", "SUCCESS")
                .addKeyValue("actor", resolveActor())
                .addKeyValue(
                        "method",
                        joinPoint.getSignature().toShortString()
                )
                .log("Business operation completed");
    }

    private String resolveActor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "anonymous";
        }

        return authentication.getName();
    }
}
