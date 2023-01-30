package com.example.demo.Service;

import com.example.demo.Entity.Device;
import com.example.demo.Entity.Order;
import com.example.demo.Entity.Order_device;
import com.example.demo.Repositories.Order_deviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Order_deviceService {

    @Autowired
    Order_deviceRepository order_deviceRepository;


    /**
     * Сохранение order_device (это один их пунктов в целом заказе)
     * @param order Заказ, к которому принадлежит эта запись
     * @param device Девайс, который пытаются приобрести
     * @param amount количество Девайсов
     */
    public void save(Order order, Device device, long amount) {

        Order_device order_device = new Order_device();
        order_device.setOrder(order);
        order_device.setDevice(device);
        order_device.setAmountOfProduct(amount);

        order_deviceRepository.save(order_device);
    }



}
