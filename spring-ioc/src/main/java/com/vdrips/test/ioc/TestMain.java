package com.vdrips.test.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by baixf on 2017/3/15.
 */
public class TestMain {

    public static void main(String args[]){
        setterDI();
        constructorDI();
        interfaceDI();
    }

    private static void setterDI(){
        // 得到ApplicationContext对象
        ApplicationContext ctx = new ClassPathXmlApplicationContext("diSetterContext.xml");
        // 得到Bean
        LoginAction loginAction = (LoginAction) ctx.getBean("loginAction");

        loginAction.execute();
    }
    private static void constructorDI(){
        // 得到ApplicationContext对象
        ApplicationContext ctx = new ClassPathXmlApplicationContext("diConstructorContext.xml");
        // 得到Bean
        LoginAction loginAction = (LoginAction) ctx.getBean("loginAction");

        loginAction.execute();
    }

    private static void interfaceDI(){
        // 得到ApplicationContext对象
        ApplicationContext ctx = new ClassPathXmlApplicationContext("diInterfaceContext.xml");
        // 得到Bean
        LoginAction loginAction = (LoginAction) ctx.getBean("loginAction");

        loginAction.executeInterface();
    }
}
