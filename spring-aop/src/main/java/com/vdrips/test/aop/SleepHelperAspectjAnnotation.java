package com.vdrips.test.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by baixf on 2017/3/14.
 * aspectj注解
 */
@Aspect
public class SleepHelperAspectjAnnotation {

    public SleepHelperAspectjAnnotation(){

    }
    @Pointcut("execution(* *.sleep())")
    public void sleeppoint(){
    }

    @Before("sleeppoint()")
    public void beforeSleep(){
        System.out.println("aspectj Annotation aop before sleep:睡前脱衣");
    }

    @AfterReturning("sleeppoint()")
    public void afterReturningSleep(){
        System.out.println("aspectj Annotation aop before sleep：睡醒起床");
    }
}
