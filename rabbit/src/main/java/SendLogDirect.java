import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * rabbit
 * Created by nantian on 2018/5/7.
 */
public class SendLogDirect {
    private static final String EXCHANGE_NAME = "ex_log_direct";
    private static final String[] SERVERITTES = {"info","warning","error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        for(int i=0;i<6;i++){
            String serverity = getServerity();
            String msg = serverity+"_log: "+ UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME,serverity,null,msg.getBytes());
            System.out.println("Send msg: "+msg);
        }
        channel.close();
        connection.close();
    }

    /**
     * 随机产生一种日志类型
     * @return
     */
    private static String getServerity() {
        Random random = new Random();
        int nextval = random.nextInt(3);
        return SERVERITTES[nextval];
    }
}
