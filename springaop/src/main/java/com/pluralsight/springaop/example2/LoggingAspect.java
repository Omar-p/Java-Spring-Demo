package com.pluralsight.springaop.example2;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {

  @Before("execution(* *.*Passenger(..))")
    public void logBefore() {
      System.out.println("Before method execution");
    }

    @After("execution(* *.*Passenger(..))")

    public void logAfter() {
      System.out.println("After method execution");
    }
}
