import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbit
 * Created by nantian on 2018/5/7.
 */
public class RecvLogTopicForKernal {
    private static final String EXCHANGE_NAME = "ex_log_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "kernal.*");
        System.out.println("Waiting for msg about kernal...");
        Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"utf-8");
                String key = envelope.getRoutingKey();
                System.out.println("Recv msg: "+msg+" ,key: "+key);

            }
        };
        channel.basicConsume(queueName,true,consumer);
    }



}
