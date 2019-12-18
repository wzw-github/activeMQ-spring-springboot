package com.wzw.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 持久状态下的订阅，能恢复一个未签收的消息
 */
public class JmsConsumer_Topic_Persist {
    //    public static final String ACTIVEMQ_URL="tcp://192.168.43.223:61616";
    public static final String ACTIVEMQ_URL="tcp://192.168.1.102:61616";
    private static final String TOPIC_NAME="topic01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("zs————————————————————");

        //1、创建连接工厂,按照给定的url地址采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接connection,并访问
        Connection connection = activeMQConnectionFactory.createConnection();
        //注册
        connection.setClientID("zs");
        //3、创建会话session,两个参数，参数1：事务，参数2：签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地（具体是队列queue还是主题topic）
        Topic topic = (Topic) session.createTopic(TOPIC_NAME);
        //创建订阅，createDurableSubscriber(topic,"....")，参数1：topic，参数2：备注
        TopicSubscriber topicSubscriber=session.createDurableSubscriber(topic,"....");
        connection.start();

        Message message=topicSubscriber.receive();
        while (null!=message){
            TextMessage textMessage= (TextMessage) message;
            System.out.println("----收到持久化topic消息："+textMessage.getText());
            message=topicSubscriber.receive(5000L);
        }
        session.close();
        connection.close();
        /**
         * 1、先运行消费者，等于向 MQ注册了，
         * 2、然后再运行生产者，无论消费者现在是否在线，都能接收到，不在线的话，下次这个消费者上线，一样能接收到
         */
    }
}
