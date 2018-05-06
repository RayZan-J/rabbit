import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Work {
    private static final String QUEUE_NAME = "taskqueue";

    public static void main(String[] args) {
        final int hash = Work.class.hashCode();
        //打开消息应答，确保每次消费都能回馈给rabbitmq，如果没有回馈，则可被重新传递给另一个生产者
        final boolean ack = false;
        //持久化
        boolean durable = true;
        //公平转发,诉RabbitMQ不要在同一时间给一个消费者超过一条消息。换句话说，只有在消费者空闲的时候会发送下一条信息
        int prefetchcount = 1;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.1");
        factory.setUsername("admin");
        factory.setPassword("123");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        try {
            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
            //公平转发
            channel.basicQos(prefetchcount);
            System.out.println("Waiting for work...");
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"utf-8");
                    System.out.println("Recevied msg: "+msg);
                    try{
                        dowork(msg);
                        System.out.println(hash+" has done --");
                        channel.basicAck(envelope.getDeliveryTag(),ack);
                    }catch (Exception e){
                        channel.basicReject(envelope.getDeliveryTag(),false);
                    }
                }
            };
            channel.basicConsume(QUEUE_NAME,consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    private static void dowork(String str){
        for(char c: str.toCharArray()){
            if(c == '.'){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
