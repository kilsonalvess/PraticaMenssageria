package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Produtor {

    private final static String QUEUE_NAME = "non_durable_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            for (int i = 1; i <= 1000000; i++) {
                String message = i + "-" + System.currentTimeMillis();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
    }
}

