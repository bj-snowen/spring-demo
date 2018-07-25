package com.vdrips.test.springrmq.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by baixf on 2017/11/30.
 */
public class QueueTwoListner implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("two data:" + Thread.currentThread().getName() + new String(message.getBody()));
    }
}
