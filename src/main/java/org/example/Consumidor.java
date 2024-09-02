package org.example;

import com.rabbitmq.client.*;

public class Consumidor {

    private final static String QUEUE_NAME = "non_durable_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String[] parts = message.split("-");
            long sentTimestamp = Long.parseLong(parts[1]);
            long receivedTimestamp = System.currentTimeMillis();
            System.out.println("Received message " + parts[0] + " with latency: " + (receivedTimestamp - sentTimestamp) + " ms");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}
