import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class SendLog {
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
        String msg = DateFormat.getDateTimeInstance().format(new Date()) +" :log time";
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
        System.out.println("send msg: "+msg);
        channel.close();
        connection.close();
    }
}
