package com.pluralsight.springaop.example4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAspect {

  @Around("@annotation(Log)")
  public Object log (ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    var methodName = proceedingJoinPoint.getSignature().getName();
    var methodsArgs = proceedingJoinPoint.getArgs();

    System.out.println("Before method execution: " + methodName + " with args: " + methodsArgs[0]);

    var res = proceedingJoinPoint.proceed();

    return res;
  }
}
