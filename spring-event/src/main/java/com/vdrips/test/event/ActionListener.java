package com.vdrips.test.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by baixf on 2017/3/14.
 * 事件监听类实现了ApplicationListener接口，必须实现onApplicationEvent方法，在这个方法中，我们可以根据业务要求，
 * 根据applicationContext.publishEvent(event)中发布的不同事件，进行相应的处理。
 */
public class ActionListener implements ApplicationListener {
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ActionEvent){
            PersonBean person = (PersonBean) applicationEvent.getSource();
            System.out.println("Name:" + person.getName());
            System.out.println("Password:" + person.getPassword());
        }
    }
}
