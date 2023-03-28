package com.example.demo.Controller.BasicController;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.Service.OrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;

@Api(tags = "Order")
@CrossOrigin("*")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "Добавление нового заказа")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 400, message = "There isn't exist Device with this id"),
            @ApiResponse(code = 400, message = "Json-формат со следующими полями:\nfield - лист полей, к которым " +
                    "относятся ошибки\ninfo - характеристика каждой ошибки")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Mono<Void> addAnOrder(
            @ApiParam(
                    value = ":id и :amount Девайсов",
                    example = "{\n\"totalSumCheck\":\"150\",\n\"orderItems\":" +
                            "[{\n\"id\":\"20\",\n\"amount\":\"3\"\n}\n,...\n]}",
                    required = true
            )
            @RequestBody String body,
            ServerHttpRequest request){
        return orderService.addAnOrder(body, request);
    }



    @ApiOperation(value = "Получение всех заказов конкретного пользователя")
    @GetMapping("/getAllByUser")
    public Flux<OrderDTO> getAllOrdersByUser(ServerHttpRequest request){
        return orderService.getAllOrdersDTOByUser(request);
    }



    @ApiOperation(value = "Получение абсолютно всех заказов")
    @GetMapping("/getAll")
    public Flux<OrderDTO> getAllOrders(){
        return orderService.getAllOrdersDTO();
    }



//    @ApiOperation(value = "Изменение статуса у заказа")
//    @PutMapping("/changeStatusOfOrder")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "---"),
//            @ApiResponse(code = 201, message = "Created"),
//            @ApiResponse(code = 400, message = "Order with this :id doesn't exist"),
//            @ApiResponse(code = 400, message = "Incorrect status")
//    })
//    public void changeStatusOfOrder(
//            @ApiParam(
//                    value = ":id Заказа и новый статус (ACTIVE/INACTIVE)",
//                    example = "{\n\"id\":\"5\",\n\"status\":\"ACTIVE\"\n}",
//                    required = true
//            )
//            @RequestBody String body
//    ){
//        orderService.changeStatusOfOrder(body);
//    }

}
