package com.wzw.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class JmsProduce_Topic_Persist {

    //    public static final String ACTIVEMQ_URL="tcp://192.168.43.223:61616";
    public static final String ACTIVEMQ_URL="tcp://192.168.1.102:61616";
    private static final String TOPIC_NAME="topic01";

    public static void main(String[] args) throws JMSException {
        //1、创建连接工厂,按照给定的url地址采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接connection,并访问
        Connection connection = activeMQConnectionFactory.createConnection();

        //3、创建会话session,两个参数，参数1：事务，参数2：签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地（具体是队列queue还是主题topic）
        Topic topic = (Topic) session.createTopic(TOPIC_NAME);
        //5、创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        //持久化的topic的start得放在这里
        connection.start();
        //6、通过使用messageProducer生产3条消息发送到MQ队列里面
        for (int i=0;i<=3;i++){
            //7、创建消息
            TextMessage textMessage = session.createTextMessage("topicMsg-----" + i);
            //8、通过messageProducer发送给mq
            messageProducer.send(textMessage);

        }
        //9、关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("topic消息发送到MQ完成");


    }
}
