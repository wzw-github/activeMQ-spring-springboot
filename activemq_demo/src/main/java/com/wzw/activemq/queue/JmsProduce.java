package com.wzw.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    //    public static final String ACTIVEMQ_URL="tcp://192.168.43.223:61616";
//    public static final String ACTIVEMQ_URL="tcp://localhost:61616";
    //配置了auto+tcp，他的端口就是61608,用nio协议或者用tcp协议都可以，会自动识别协议，因为不同的协议代码可能不一样，
    // 所以除了nio和tcp，其它协议可能会报错，如果单独配置了nio协议，它的端口就是61618
    public static final String ACTIVEMQ_URL="nio://192.168.43.223:61608";
    private static final String QUEUE_NAME="queue01";
    private static final String TOPIC_NAME="topic01";

    public static void main(String[] args) throws JMSException {
        //1、创建连接工厂,按照给定的url地址采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接connection,并访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3、创建会话session,两个参数，参数1：事务（如果是true,需要connection.commit();才能发送），参数2：签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地（具体是队列queue还是主题topic）
        Queue queue = (Queue) session.createQueue(QUEUE_NAME);
        //5、创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        //6、通过使用messageProducer生产3条消息发送到MQ队列里面
        for (int i=0;i<=3;i++){
            //7、创建消息
            TextMessage textMessage = session.createTextMessage("QueueMsg-----" + i);
            //8、通过messageProducer发送给mq
            messageProducer.send(textMessage);

        }

        /*
            如果事务为true，就需要手动提交，如果是批量的数据，要么成功要么失败，可以catch中回滚
        try {
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
            session.rollback();
        }*/


        //9、关闭资源
        messageProducer.close();
        session.close();
        connection.close();





        System.out.println("Queue消息发送到MQ完成");


    }
}
