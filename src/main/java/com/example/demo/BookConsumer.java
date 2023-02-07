package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BookConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookConsumer.class);

    @KafkaListener(topics = "test-topic")
    public void receiveMessage(@Payload BookAvro book) {
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        LOGGER.info("Received message: key = {}, value = {}", book);
    }
}