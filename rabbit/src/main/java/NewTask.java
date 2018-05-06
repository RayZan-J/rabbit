import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NewTask {
    private static final String QUEUE_NAME = "taskqueue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        boolean durable  = true;
        factory.setHost("192.168.56.1");
        factory.setUsername("admin");
        factory.setPassword("123");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
            for(int i=9;i>=0;i--){
                StringBuilder str = new StringBuilder();
                for (int j=0;j<i;j++){
                    str.append(".");
                }
                String msg = "hello world "+str+str.length();
                channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
                System.out.println("Send msg: "+msg);
            }
            channel.close();
            connection.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
