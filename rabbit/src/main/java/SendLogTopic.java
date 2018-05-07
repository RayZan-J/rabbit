import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * rabbit
 * Created by nantian on 2018/5/7.
 */
public class SendLogTopic {
    private static final String EXCHANGE_NAME = "ex_log_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        String[] routingKey = new String[]{
                "kernal.info","cron.waring","auth.info","kernal.critical"
        };
        for(String key: routingKey){
            String msg = UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME,key,null,msg.getBytes());
            System.out.println("Send routing key: "+key+" and msg: "+msg);
        }
        channel.close();
        connection.close();
    }
}
