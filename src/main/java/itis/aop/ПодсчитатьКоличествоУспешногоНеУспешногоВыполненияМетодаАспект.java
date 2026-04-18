package itis.aop;

import itis.repository.ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаАспект {

    private final ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище подсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище;

    @Pointcut("@annotation(ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетода)")
    public void подсчитатьУспешноНеУспешноAnnotatedMethods() {

    }

    @Around("подсчитатьУспешноНеУспешноAnnotatedMethods()")
    public Object подсчитать(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        try {
            result = joinPoint.proceed();
            подсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище.увеличитьУспех(methodName);
        } catch (Throwable e) {
            подсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище.увеличитьОшибки(methodName);
            throw e;

        }
        return result;
    }


}
