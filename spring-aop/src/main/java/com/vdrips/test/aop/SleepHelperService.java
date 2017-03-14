package com.vdrips.test.aop;

import java.lang.reflect.Method;

/**
 * Created by baixf on 2017/3/14.
 */
public class SleepHelperService {
    public void doAfter() throws Throwable {
        System.out.println("auto service aop afterReturning 睡醒起床穿衣");
    }

    public void dobBefore() throws Throwable {
        System.out.println("auto service aop before 睡觉前脱衣");
    }
}
