package com.example.demo.Kafka.Message;

import com.example.demo.Entity.Order_device;
import com.example.demo.Repositories.DeviceRepository;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Data
public class DeviceInOrder {

    private Long deviceId;
    private String name;
    private Long brandId;
    private Long amountOfProduct;


    public static Mono<DeviceInOrder> createEntity(Order_device order_device, DeviceRepository deviceRepository)  {
        DeviceInOrder deviceInOrder = new DeviceInOrder();
        deviceInOrder.setDeviceId(order_device.getDeviceId());
        deviceInOrder.setAmountOfProduct(order_device.getAmountOfProduct());
        return deviceRepository
                .findById(order_device.getDeviceId())
                .publishOn(Schedulers.boundedElastic())
                .map(device -> {
                    deviceInOrder.setName(device.getName());
                    deviceInOrder.setBrandId(device.getBrandId());
                    return deviceInOrder;
                });
    }

    public static Flux<DeviceInOrder> createEntities(List<Order_device> list, DeviceRepository deviceRepository) {
        List<DeviceInOrder> res = new ArrayList<>();
        return Mono
                .just(list)
                .flatMapMany(Flux::fromIterable)
                .publishOn(Schedulers.boundedElastic())
                .mapNotNull(order_device -> createEntity(order_device, deviceRepository).block());

    }

}
