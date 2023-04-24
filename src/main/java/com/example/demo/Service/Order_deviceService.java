package com.example.demo.Service;

import com.example.demo.Entity.Order_device;
import com.example.demo.Repositories.Order_deviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class Order_deviceService {

    @Autowired
    Order_deviceRepository order_deviceRepository;


    /**
     * Сохранение order_device (это один их пунктов в целом заказе)
     * @param orderId Заказ, к которому принадлежит эта запись
     * @param deviceId Девайс, который пытаются приобрести
     * @param amount количество Девайсов
     */
    public Order_device createOrder_device(Long orderId, Long deviceId, long amount) {
        Order_device order_device = new Order_device();
        order_device.setOrderId(orderId);
        order_device.setDeviceId(deviceId);
        order_device.setAmountOfProduct(amount);
        return order_device;
    }


    public Flux<Order_device> saveAll(List<Order_device> order_devices) {
        return order_deviceRepository
                .saveAll(order_devices);
//                .then(Mono.empty());
    }
}
