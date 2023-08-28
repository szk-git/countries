package com.countries.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.countries.controller.CountryController.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();
        logger.info("Request from IP: {}. Entering method: {}", ipAddress, joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        logger.info("Exiting method: {}", joinPoint.getSignature().getName());
        return result;
    }

    @AfterReturning(pointcut = "execution(* com.countries.controller.CountryController.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;
            logger.info("Method {} returned with status: {}", joinPoint.getSignature().getName(), response.getStatusCode());
        }
    }
}
