package com.vdrips.test.aop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Created by baixf on 2017/3/14.
 * 通知类 advice
 */
public class SleepHelper implements MethodBeforeAdvice,AfterReturningAdvice {

    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("aop afterReturning 睡醒起床穿衣");
    }

    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("aop before 睡觉前脱衣");
    }
}
