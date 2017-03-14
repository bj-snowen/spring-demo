package com.vdrips.test.event;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by baixf on 2017/3/14.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:eventContext.xml")
public class TestEvent {

//    @Autowired
//    ActionLogic actionLogic;

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("eventContext.xml");
        ActionLogic logic = (ActionLogic) ctx.getBean("actionLogic");
        logic.execute();
    }

//    @Test
//    public void event(){
//        actionLogic.execute();
//    }
}
