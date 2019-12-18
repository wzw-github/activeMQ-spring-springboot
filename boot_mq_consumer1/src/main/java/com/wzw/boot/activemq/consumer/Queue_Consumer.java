package com.wzw.boot.activemq.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.UUID;

@Component
public class Queue_Consumer {

    @JmsListener(destination = "${myqueue}")
    public void recevice(TextMessage textMessage) throws JMSException {
        System.out.println("************消费者收到消息："+textMessage.getText());
    }



}
