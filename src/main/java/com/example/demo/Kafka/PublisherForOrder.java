package com.example.demo.Kafka;

import com.example.demo.Entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PublisherForOrder {
    private static final String TOPIC = "order_topic";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void sendMessage(String topic, Order message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(topic, message);
    }

    @Scheduled(fixedDelay = 5000)
    public void getWeatherInfoJob() {
        logger.info("generate fake weather event");
        // fake event
        Order event = new Order();
        event.setId(777L);
        event.setTotalSumCheck(5000L);

        sendMessage(TOPIC, event);
    }

}
