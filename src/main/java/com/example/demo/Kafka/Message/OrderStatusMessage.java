package com.example.demo.Kafka.Message;

import lombok.Data;

@Data
public class OrderStatusMessage {

    private String id;
    private String status;

}
