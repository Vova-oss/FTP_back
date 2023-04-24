package com.example.demo.Kafka.Message;

import com.example.demo.Entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderMessage {

    private Long id;
    private Long userId;
    private String status;
    private Long totalSumCheck;
    private Long dataOfCreate;
    private List<DeviceInOrder> devices;


    public static OrderMessage createEntity(Order order){
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setId(order.getId());
        orderMessage.setUserId(order.getUserId());
        orderMessage.setStatus(order.getStatus());
        orderMessage.setTotalSumCheck(order.getTotalSumCheck());
        orderMessage.setDataOfCreate(order.getDataOfCreate());
        return orderMessage;
    }


}
