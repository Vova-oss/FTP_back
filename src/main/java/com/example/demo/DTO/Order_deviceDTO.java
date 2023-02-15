//package com.example.demo.DTO;
//
//import com.example.demo.Entity.Order_device;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Data
//public class Order_deviceDTO {
//
//    @ApiModelProperty(notes = "Девайс в заказе", name = "deviceResponseModel", required = true)
//    private DeviceDTO deviceResponseModel;
//
//    @ApiModelProperty(notes = "Количество данного Девайса в заказе", name = "amountOfProduct",
//            required = true, example = "7")
//    private Long amountOfProduct;
//
//    public static Order_deviceDTO create(Order_device order_device){
//        Order_deviceDTO order_deviceDTO = new Order_deviceDTO();
//        order_deviceDTO.setDeviceResponseModel(DeviceDTO.create(order_device.getDeviceId()));
//        order_deviceDTO.setAmountOfProduct(order_device.getAmountOfProduct());
//        return order_deviceDTO;
//    }
//
//    public static List<Order_deviceDTO> createList(List<Order_device> list){
//        List<Order_deviceDTO> newList = new LinkedList<>();
//        for(Order_device order_device: list){
//            newList.add(create(order_device));
//        }
//        return newList;
//    }
//
//}
