package com.example.demo.Kafka;

import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.Entity.Order;
import com.example.demo.Entity.Order_device;
import com.example.demo.Kafka.Message.DeviceInOrder;
import com.example.demo.Kafka.Message.OrderMessage;
import com.example.demo.Repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublisherForOrder {
    private static final String TOPIC = "order_topic";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;
    @Autowired
    DeviceRepository deviceRepository;

    private void sendMessage(String topic, OrderMessage message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(topic, message);
    }

    public Mono<Void> createOrderMessage(Order order, List<Order_device> order_device) {
        try {
            OrderMessage orderMessage = OrderMessage.createEntity(order);
            return DeviceInOrder
                    .createEntities(order_device, deviceRepository)
                    .collectList()
                    .map(deviceInOrders -> {
                        orderMessage.setDevices(deviceInOrders);
                        sendMessage(TOPIC, orderMessage);
                        return deviceInOrders;
                    }).then(Mono.empty());
        }catch (Exception e){
            return Mono.error(() -> new CustomException("Some exception while sending message to Kafka"));
        }
    }

//    @Scheduled(fixedDelay = 5000)
//    public void getWeatherInfoJob() {
//        logger.info("generate fake weather event");
//        // fake event
//        Order event = new Order();
//        event.setId(777L);
//        event.setTotalSumCheck(5000L);
//
//        sendMessage(TOPIC, event);
//    }

}
