package com.vdrips.test.springrmq.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.tools.json.JSONUtil;
import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProducerMq {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendDataToCrQueue(Object obj) {
        Map map = new HashMap();
        map.put("key", 1);
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        map.put("queue", list);

        amqpTemplate.convertAndSend("queue_one_key", map);

        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend("my-mq-exchange", "queue_one_key", "hello 你好" + i);
            amqpTemplate.convertAndSend("my-mq-exchange", "queue_two_key", "hello 你好" + i);
            amqpTemplate.convertAndSend("my-fanout-exchange", "", "hello 你好" + i);
        }
    }

}