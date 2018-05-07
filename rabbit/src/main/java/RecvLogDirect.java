import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * rabbit
 * Created by nantian on 2018/5/7.
 */
public class RecvLogDirect {
    private static final String EXCHANGE_NAME = "ex_log_direct";
    private static final String[] SERVERITTES = {"info","warning","error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        String serverity = getServerity();
        channel.queueBind(queueName, EXCHANGE_NAME, serverity);
        System.out.println("Waiting for get log...");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"utf-8");
                System.out.println("Recv log: "+msg);
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }


    /**
     * 随机产生一种日志类型
     * @return
     */
    private static String getServerity() {
        Random random = new Random();
        int nextval = random.nextInt(3);
        System.out.println(nextval);
        return SERVERITTES[nextval];
    }
}
