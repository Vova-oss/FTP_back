package com.example.demo.Kafka;

import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.Entity.Order;
import com.example.demo.Entity.Order_device;
import com.example.demo.Kafka.Message.DeviceInOrder;
import com.example.demo.Kafka.Message.OrderMessage;
import com.example.demo.Kafka.Message.OrderStatusMessage;
import com.example.demo.Repositories.DeviceRepository;
import com.example.demo.Repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.util.List;

@Service
public class PublisherForOrder {
    private static final String TOPIC = "order_topic";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaReceiver<String, String> kafkaReceiver;
    @Autowired
    private KafkaSender<String, OrderMessage> kafkaSender;
    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    OrderRepository orderRepository;


    private void sendMessage(OrderMessage message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        kafkaSender.send(Mono.just(SenderRecord.create(new ProducerRecord<>(TOPIC, message), null))).then().subscribe();
    }

    public Mono<Void> createOrderMessage(Order order, List<Order_device> order_device) {
        try {
            OrderMessage orderMessage = OrderMessage.createEntity(order);
            return DeviceInOrder
                    .createEntities(order_device, deviceRepository)
                    .collectList()
                    .map(deviceInOrders -> {
                        orderMessage.setDevices(deviceInOrders);
                        sendMessage(orderMessage);
                        return deviceInOrders;
                    }).then(Mono.empty());
        }catch (Exception e){
            return Mono.error(() -> new CustomException("Some exception while sending message to Kafka"));
        }
    }

    @PostConstruct()
    public void listen() {
        kafkaReceiver
                .receive()
                .flatMap(record -> {
                    // обработка сообщения
                    try {
                        OrderStatusMessage osm = new ObjectMapper().readValue(record.value(), OrderStatusMessage.class);
                        return orderRepository.updateStatusById(Long.valueOf(osm.getId()), osm.getStatus());
                    } catch (JsonProcessingException ignored) {}
                    return Mono.just(record);
                })
                .subscribe();
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
