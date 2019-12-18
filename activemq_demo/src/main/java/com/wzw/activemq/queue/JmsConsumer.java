package com.wzw.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer {

    //    public static final String ACTIVEMQ_URL="tcp://192.168.43.223:61616";
//    public static final String ACTIVEMQ_URL="tcp://localhost:61616";
    //配置了auto+tcp，他的端口就是61608,用nio协议或者用tcp协议都可以，会自动识别协议，因为不同的协议代码可能不一样，
    // 所以除了nio和tcp，其它协议可能会报错，如果单独配置了nio协议，它的端口就是61618
    public static final String ACTIVEMQ_URL="nio://192.168.43.223:61608";
    private static final String QUEUE_NAME="queue01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("二号消费者————————————————————");

        //1、创建连接工厂,按照给定的url地址采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接connection,并访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3、创建会话session,两个参数，参数1：事务，参数2：签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);


       /*
           同步阻塞方式recevice()
           订阅者或接收者调用MessageConsumer的receive()方法来接收消息，receive方法在能够接收到消息前或超时前一直阻塞
       while (true){
            //接收消息，receive()：一直等待，receive(4000L)：等待4秒，如果没有消息，就关闭这个消费者
            //TextMessage textMessage = (TextMessage)messageConsumer.receive();
            TextMessage textMessage = (TextMessage)messageConsumer.receive(4000L);
            if(null!=textMessage){
                System.out.println("消费者接收到消息："+textMessage.getText());
            }else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();*/
       //通过监听的方式来消费消息 MessageConsumer messageConsumer = session.createConsumer(queue);
        //异步阻塞方式：监听器onMessage（）
        //订阅者或接收者通过MessageConsumer的setMessageListener（MessageListener listener）注册一个消息监听器
        //当消息到达后，系统自动调用监听器MessageListener的onMessage（Message message）方法
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(null!=message&&message instanceof TextMessage){
                    TextMessage textMessage=(TextMessage)message;
                    try {
                        System.out.println("MessageListener接收："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
                if(null!=message&&message instanceof MapMessage){
                    MapMessage mapMessage=(MapMessage)message;
                    try {
                        System.out.println("MessageListener接收："+mapMessage.getString("k1"));
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //控制台不熄灭，如果不加这句，监听还没来得及执行，下面的close就执行了。不会报错，但是消息也不会被消费
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();
        System.out.println("消息发布完成");


        /**
         * 1 先生产，只启动1号消费者，问题：1号消费者能消费消息吗
         * Y
         * 2 先生产，只启动1号消费者，再启动2号消费者，问题：2号消费者还能消费消息吗
         *      2.1     Y
         *      2.2     N
         * 3 先启动两个消费者，再生产两次消息共8条，消费情况如何
         *      3.1 2个消费者都有4条   N
         *      3.2 先到先得，8条全给一个 N
         *      3.3 一人一半    Y
         */

    }

}
