package itis.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* itis..*.*(..)) && " +
              "!within(itis.dto..*) && " +
              "!within(itis.model..*) && " +
              "!within(itis.config..*) && " +
              "!within(itis.properties..*)")
    public void logExecution() {

    }

    @Pointcut("@annotation(Loggable)")
    public void logAnnotatedMethods() {

    }

    @Around("logExecution()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();

        log.info("Start execution {}.{}", className, methodName);
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception in {}.{}: {}", className, methodName, e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Finish execution {}.{}", className, methodName);
        return result;

    }
}
