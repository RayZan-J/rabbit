import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RecvLogToConsole {
    private static final String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.1");
        factory.setUsername("admin");
        factory.setPassword("123");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("Waiting for msg...");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body,"utf-8");
                try{
                    doSomething(str);
                }catch (Exception e){
                    channel.basicReject(envelope.getDeliveryTag(),false);
                }
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }

    private static void doSomething(String str) throws IOException {
        System.out.println("Recving msg: "+str);
    }
}
