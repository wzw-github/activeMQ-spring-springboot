package com.wzw.boot.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class MainApp_Consumer {
    public static void main(String[] args) {
        SpringApplication.run(MainApp_Consumer.class,args);
    }
}
