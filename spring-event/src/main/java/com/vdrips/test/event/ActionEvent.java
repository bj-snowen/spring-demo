package com.vdrips.test.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by baixf on 2017/3/14.
 * 必须继承Spring的 ApplicationEvent
 */
public class ActionEvent extends ApplicationEvent {

    public ActionEvent(Object source) {
        super(source);
    }
}
