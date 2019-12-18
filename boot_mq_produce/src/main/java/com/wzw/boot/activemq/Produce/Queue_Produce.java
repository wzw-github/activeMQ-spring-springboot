package com.wzw.boot.activemq.Produce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.UUID;

@Component
public class Queue_Produce {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public void produceMsg(){
        jmsMessagingTemplate.convertAndSend(queue,"********:"+ UUID.randomUUID().toString().substring(0,6));
    }

    //间隔时间3秒定投
    @Scheduled(fixedDelay = 3000)
    public void produceMsgScheduld(){
        jmsMessagingTemplate.convertAndSend(queue,"********Scheduled:"+ UUID.randomUUID().toString().substring(0,6));
        System.out.println("************produceMsgScheduld   end   OK");
    }

}
