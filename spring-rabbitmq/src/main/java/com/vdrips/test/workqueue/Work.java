package com.vdrips.test.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.concurrent.TimeoutException;

/**
 * Created by baixf on 2017/3/21.
 * 平均的每个消费者将会获得相等数量的消息。这样分发消息的方式叫做round-robin。
 * 执行一个任务需要花费几秒钟。你可能会担心当一个工作者在执行任务时发生中断。我们上面的代码，一旦RabbItMQ交付了一个信息给消费者，会马上从内存中移除这个信息。
 * 在这种情况下，如果杀死正在执行任务的某个工作者，我们会丢失它正在处理的信息。我们也会丢失已经转发给这个工作者且它还未执行的消息。
 *
 *
 * 公平转发（Fair dispatch）
 * 或许会发现，目前的消息转发机制（Round-robin）并非是我们想要的。例如，这样一种情况，对于两个消费者，有一系列的任务，奇数任务特别耗时，而偶数任务却很轻松，这样造成一个消费者一直繁忙，另一个消费者却很快执行完任务后等待。
 * 造成这样的原因是因为RabbitMQ仅仅是当消息到达队列进行转发消息。并不在乎有多少任务消费者并未传递一个应答给RabbitMQ。仅仅盲目转发所有的奇数给一个消费者，偶数给另一个消费者。
 * 为了解决这样的问题，我们可以使用basicQos方法，传递参数为prefetchCount = 1。这样告诉RabbitMQ不要在同一时间给一个消费者超过一条消息。换句话说，只有在消费者空闲的时候会发送下一条信息。
 * [java] view plain copy
 * int prefetchCount = 1;
 * channel.basicQos(prefetchCount);
 */
public class Work {
    //队列名称
    private final static String QUEUE_NAME = "workqueue";

    public static void main(String[] argv) throws java.io.IOException,
            java.lang.InterruptedException, TimeoutException {
        //区分不同工作进程的输出
        int hashCode = Work.class.hashCode();
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列 持久化
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(hashCode
                + " [*] Waiting for messages. To exit press CTRL+C");

        //设置最大服务转发消息数量 公平转发（Fair dispatch） 不设置为 消息转发机制（Round-robin）
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        boolean messageAcknowledgments = true;
        if (messageAcknowledgments){
            // 指定消费队列
            channel.basicConsume(QUEUE_NAME, true, consumer);
            while (true)
            {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());

                System.out.println(hashCode + " [x] Received '" + message + "'");
                doWork(message);
                System.out.println(hashCode + " [x] Done");

            }
        } else {
//        当某个工作者（接收者）被杀死时，我们希望将任务传递给另一个工作者。
//        为了保证消息永远不会丢失，RabbitMQ支持消息应答（message acknowledgments）。消费者发送应答给RabbitMQ，告诉它信息已经被接收和处理，然后RabbitMQ可以自由的进行信息删除。
//        如果消费者被杀死而没有发送应答，RabbitMQ会认为该信息没有被完全的处理，然后将会重新转发给别的消费者。通过这种方式，你可以确认信息不会被丢失，即使消者偶尔被杀死。
//        这种机制并没有超时时间这么一说，RabbitMQ只有在消费者连接断开是重新转发此信息。如果消费者处理一个信息需要耗费特别特别长的时间是允许的。
//        消息应答默认是打开的。上面的代码中我们通过显示的设置autoAsk=true关闭了这种机制。下面我们修改代码（Work.java）：
//
//        [java] view plain copy
//        boolean ack = false ; //打开应答机制
//        channel.basicConsume(QUEUE_NAME, ack, consumer);
////另外需要在每次处理完成一个消息后，手动发送一次应答。
//        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            // 指定消费队列
            boolean ack = false ; //打开应答机制
            channel.basicConsume(QUEUE_NAME, ack, consumer);
            while (true)
            {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());

                System.out.println(hashCode + " [x] Received '" + message + "'");
                doWork(message);
                System.out.println(hashCode + " [x] Done");
                //发送应答
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            }
        }

    }

    /**
     * 每个点耗时1s
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException
    {
        for (char ch : task.toCharArray())
        {
            if (ch == '.')
                Thread.sleep(10000);
        }
    }
}
