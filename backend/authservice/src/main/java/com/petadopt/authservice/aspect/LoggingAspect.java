package com.petadopt.authservice.aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    @Around("execution(* com.petadopt.authservice.controller..*(..))")
    public Object logExecutionTimeAndPayload(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Incoming API Request: {} | Payload: {}", methodName, args);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("API Error in {}: {}", methodName, throwable.getMessage());
            throw throwable;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Outgoing API Response from {} | Time: {}ms | Result: {}", methodName, executionTime, result);

        return result;
    }
}

