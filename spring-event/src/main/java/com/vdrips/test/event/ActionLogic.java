package com.vdrips.test.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by baixf on 2017/3/14.
 * 必须实现ApplicationContextAware接口,这里最关键就是applicationContext的publishEvent方法，它起到发布事件，通知Listener的目的。
 */
public class ActionLogic implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void execute(){
        PersonBean person = new PersonBean();
        person.setName("username");
        person.setPassword("123456");
        ActionEvent event = new ActionEvent(person);

        this.applicationContext.publishEvent(event);

    }
}
