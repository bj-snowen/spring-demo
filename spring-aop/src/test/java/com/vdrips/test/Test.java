package com.vdrips.test;

import com.vdrips.test.aop.Sleepable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by baixf on 2017/3/14.
 */
public class Test {

    public static void main(String[] args){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Sleepable sleepable = (Sleepable) applicationContext.getBean("humanProxy");
        sleepable.sleep();
    }
}
