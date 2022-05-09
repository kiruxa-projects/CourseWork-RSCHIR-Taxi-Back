package com;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@org.aspectj.lang.annotation.Aspect
public class Aspect {

    @Pointcut("within(com.services.*)")
    public void executionServiceMethod() {}

    @Around("executionServiceMethod()")
    public Object timeExecutionServiceMethod(ProceedingJoinPoint jp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object object = jp.proceed();
        long end = System.currentTimeMillis();
        log.info(jp.getSignature().toShortString() + " completed in " + (end - begin) + " ms" );
        return object;
    }
}
