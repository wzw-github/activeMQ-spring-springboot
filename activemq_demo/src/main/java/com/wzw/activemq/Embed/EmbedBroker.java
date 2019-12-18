package com.wzw.activemq.Embed;

import org.apache.activemq.broker.BrokerService;

/**
 * 迷你版的mq，直接通过java代码实现
 */
public class EmbedBroker {
    public static void main(String[] args) throws Exception {
        //broker 就是实现了用代码形式启动 ActiveMQ 将 MQ 内嵌到 Java 代码中，可以随时启动，节省资源，提高了可靠性。
        //就是将 MQ 服务器作为了 Java 对象
        BrokerService brokerService=new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }
}
