package com.vdrips.test;

import com.vdrips.test.aop.Human;
import com.vdrips.test.aop.Sleepable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by baixf on 2017/3/14.
 */
public class Test {

    public static void main(String[] args){
//        handProxy();//手动配置 aop 代理
//        autoProxy();//手动配置aop 自动创建代理
        aspectjAnnotation();
//        autoAspectj();
    }


    private static void handProxy(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-handProxy.xml");
        Sleepable sleepable = (Sleepable) applicationContext.getBean("humanProxy");
        sleepable.sleep();
    }

    private static void autoProxy(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-autoProxy.xml");
        Sleepable sleepable = (Sleepable) applicationContext.getBean("human");
        sleepable.sleep();
    }

    private static void aspectjAnnotation(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-aspectjAnnotation.xml");
        Sleepable sleepable = (Sleepable) applicationContext.getBean("human");
        sleepable.sleep();
    }

    private static void autoAspectj(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-autoAspectj.xml");
        Sleepable sleepable = (Sleepable) applicationContext.getBean("human");
        sleepable.sleep();
    }
}
