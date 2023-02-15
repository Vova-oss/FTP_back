//package com.example.demo.DTO;
//
//import com.example.demo.Entity.Enum.EStatusOfOrder;
//import com.example.demo.Entity.Order;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.util.LinkedList;
//import java.util.List;
//
//
//@Data
//public class OrderDTO {
//
//    @ApiModelProperty(notes = ":id Заказа", name = "id", required = true, example = "13")
//    private Long id;
//
//    @ApiModelProperty(notes = "Ссылки на order-devicesDTO", name = "order_deviceResponseModels", required = true)
//    private List<Order_deviceDTO> order_deviceResponseModels;
//
//    @ApiModelProperty(notes = "Статус заказа (ACTIVE/INACTIVE)", name = "status", required = true, example = "ACTIVE")
//    private EStatusOfOrder status;
//
//    @ApiModelProperty(notes = "Общая сумма заказа", name = "totalSumCheck", required = true, example = "1950")
//    private Long totalSumCheck;
//
//    @ApiModelProperty(notes = "Дата создания заказа", name = "dataOfCreate", required = true)
//    private Long dataOfCreate;
//
//    public static OrderDTO create(Order order){
//        OrderDTO orderDTO = new OrderDTO();
//        orderDTO.setId(order.getId());
//        orderDTO.setOrder_deviceResponseModels(Order_deviceDTO.createList(order.getOrder_devices()));
//        orderDTO.setStatus(order.getStatus());
//        orderDTO.setTotalSumCheck(order.getTotalSumCheck());
//        orderDTO.setDataOfCreate(order.getDataOfCreate());
//        return orderDTO;
//    }
//
//    public static List<OrderDTO> createList(List<Order> list){
//        List<OrderDTO> newList = new LinkedList<>();
//        for(Order order: list){
//            newList.add(create(order));
//        }
//        return newList;
//    }
//
//}
//
//
