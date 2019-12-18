package com.wzw.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;


/**
 * 非持久状态下的订阅，不能恢复一个未签收的消息
 */
public class JmsConsumer_Topic {
//    public static final String ACTIVEMQ_URL="tcp://192.168.43.223:61616";
    public static final String ACTIVEMQ_URL="tcp://192.168.1.102:61616";
    private static final String TOPIC_NAME="topic01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("3号消费者————————————————————");

        //1、创建连接工厂,按照给定的url地址采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接connection,并访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3、创建会话session,两个参数，参数1：事务，参数2：签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地（具体是队列queue还是主题topic）
        Topic topic = (Topic) session.createTopic(TOPIC_NAME);
        //5、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        //通过监听的方式来消费消息
        /*messageConsumer.setMessageListener(new MessageListener() {
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
            }
        });*/
        messageConsumer.setMessageListener((message) -> {
            if(null!=message&&message instanceof TextMessage){
                TextMessage textMessage=(TextMessage)message;
                try {
                    System.out.println("MessageListener接收Topic消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
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
