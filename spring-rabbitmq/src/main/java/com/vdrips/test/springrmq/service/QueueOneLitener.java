package com.vdrips.test.springrmq.service;

import org.springframework.amqp.core.Message;

import org.springframework.amqp.core.MessageListener;

public class QueueOneLitener implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("one data :" +Thread.currentThread().getName() + new String(message.getBody()));
	}

}