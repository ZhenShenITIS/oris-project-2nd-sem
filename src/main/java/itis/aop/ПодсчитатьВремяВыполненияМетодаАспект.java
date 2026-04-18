package itis.aop;

import itis.repository.ВремяВыполненияМетодаХранилище;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ПодсчитатьВремяВыполненияМетодаАспект {

    private final ВремяВыполненияМетодаХранилище времяВыполненияМетодаХранилище;

    @Pointcut("@annotation(ПодсчитатьВремяВыполненияМетода)")
    public void benchmarkAnnotatedMethods() {
    }

    @Around("benchmarkAnnotatedMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();

        long startTime = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            времяВыполненияМетодаХранилище.добавитьВремяВыполнения(methodName, executionTime);
            return result;
        } catch (Throwable e) {
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            времяВыполненияМетодаХранилище.добавитьВремяВыполнения(methodName, executionTime);
            throw e;
        }
    }
}
