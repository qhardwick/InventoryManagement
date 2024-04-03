package com.skillstorm.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {


    @Around("allMethods()")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Object log(ProceedingJoinPoint pjp) throws Throwable {

        Object result = null;
        // Get the logger for the class being called:
        Logger log = LoggerFactory.getLogger(pjp.getTarget().getClass());

        // Log the method call and its arguments:
        log.trace("Method called: " + pjp.getSignature().getName());
        log.trace("Arguments: " + Arrays.toString(pjp.getArgs()));

        // Execute the method. If an exception is thrown, log it, but then rethrow it so that it can be handled by the controller advice:
        try {
            result = pjp.proceed();
        } catch (Throwable t) {
            logError(log, t);
            throw t;
        }

        // Log the method's return value:
        log.trace("Returning: " + result);
        return result;
    }

    // Log the exception:
    private void logError(Logger log, Throwable t) {
        log.error("Method threw exception: " + t);
        for (StackTraceElement e : t.getStackTrace()) {
            log.warn(e.toString());
        }
        if (t.getCause() != null) {
            logError(log, t.getCause());
        }
    }

    @Pointcut("execution( * com.skillstorm..*(..) )")
    private void allMethods() {
        /* empty method for hook */
    }


}
