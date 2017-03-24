package com.vdrips.test.workqueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by baixf on 2017/3/21.
 * 发送端
 */
public class NewTask {
    //队列名称
    private final static String QUEUE_NAME = "workqueue";

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列
        boolean durable = true;// 1、设置队列持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        //发送10条消息，依次在消息后面附加1-10个点
        for (int i = 0; i < 10; i++) {
            String dots = "";
            for (int j = 0; j <= i; j++) {
                dots += ".";
            }

            String message = "helloworld" + dots + dots.length();
            // MessageProperties 2、设置消息持久化
            channel.basicPublish("", QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        //关闭频道和资源
        channel.close();
        connection.close();
    }

//    消息持久化（Message durability）
//    我们已经学习了即使消费者被杀死，消息也不会被丢失。但是如果此时RabbitMQ服务被停止，我们的消息仍然会丢失。
//    当RabbitMQ退出或者异常退出，将会丢失所有的队列和信息，除非你告诉它不要丢失。我们需要做两件事来确保信息不会被丢失：我们需要给所有的队列和消息设置持久化的标志。
//    第一， 我们需要确认RabbitMQ永远不会丢失我们的队列。为了这样，我们需要声明它为持久化的。
//    boolean durable = true;
//    channel.queueDeclare("task_queue", durable, false, false, null);
//    注：RabbitMQ不允许使用不同的参数重新定义一个队列，所以已经存在的队列，我们无法修改其属性。
//    第二， 我们需要标识我们的信息为持久化的。通过设置MessageProperties（implements BasicProperties）值为PERSISTENT_TEXT_PLAIN。
//            channel.basicPublish("", "task_queue",MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
//    现在你可以执行一个发送消息的程序，然后关闭服务，再重新启动服务，运行消费者程序做下实验。
}
