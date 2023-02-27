package com.pluralsight.springaop.example5;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class ExceptionAspect {

  @AfterThrowing(pointcut = "execution(* com.pluralsight.springaop.example5.*.*(..))", throwing = "e")
    public void logException(Exception e) {
      System.out.println("55Exception: " + e.getMessage());
    }
}
