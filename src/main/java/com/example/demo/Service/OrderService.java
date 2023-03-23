package com.example.demo.Service;


import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Enum.EStatusOfOrder;
import com.example.demo.Entity.Order;
import com.example.demo.Entity.Order_device;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repositories.OrderRepository;
import com.example.demo.Repositories.UserRepo;
import com.example.demo.Security.Service.JWTokenService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.demo.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class OrderService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    DeviceService deviceService;
    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    Order_deviceService order_deviceService;

    @Autowired
    OrderRepository orderRepository;

    /**
     * Добавление нового Заказа
     * @param body [json] содержит:
     *             totalSumCheck - общая сумма в заказе
     *             orderItems - массив Девайсов в заказе. Один элемент массива содержит:
     *                  id - :id Девайса
     *                  amount - их количество
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - Incorrect validation (ValidationService.class)
     */
    public Mono<Void> addAnOrder(String body, ServerHttpRequest request){
        List<String> headersList = request.getHeaders().get(HEADER_JWT_STRING);
        String tokenWithPrefix = null;
        if(headersList != null)
            tokenWithPrefix = headersList.get(0);

        if(tokenWithPrefix != null) {
            String telephoneNumber = jwTokenService.
                    getNameFromJWT(tokenWithPrefix.replace(TOKEN_PREFIX, ""));

            JSONObject object;
            JSONArray array;
            long totalSumCheck;
            try {
                object = new JSONObject(body);
                array = object.getJSONArray("orderItems");
                totalSumCheck = object.getLong("totalSumCheck");
            } catch (JSONException e) {
                return Mono.error(() -> new CustomException("Incorrect JSON"));
            }

            return userRepo
                    .findByUsername(telephoneNumber)
                    .switchIfEmpty(Mono.error(new CustomException("Incorrect jwt-token")))
                    .flatMap(userEntity -> {
                        return createAndSaveNewOrder(userEntity, totalSumCheck)
                                .flatMap(order -> {
                                    return createAndSaveOrder_device(array, order.getId()).then(Mono.empty());
                                })
                        ;
                    })
                    .then(Mono.empty());
        }
        return Mono.error(() -> new CustomException("Authorization header is broken"));
    }


    /**
     * Создание новой записи о Заказе в БД
     * @param user Пользователь, которому принадлежит данный Заказ
     * @param totalSumCheck общая сумма Заказа
     * @return созданный Заказ в БД (с :id)
     *
     * @code 400 - Incorrect validation (ValidationService.class)
     */
    public Mono<Order> createAndSaveNewOrder(UserEntity user, Long totalSumCheck){
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus(EStatusOfOrder.ACTIVE.name());
        order.setTotalSumCheck(totalSumCheck);
        order.setDataOfCreate(System.currentTimeMillis());
        return orderRepository.save(order);
    }

    private Mono<Void> createAndSaveOrder_device(JSONArray array, Long orderId){
        List<Order_device> order_devices = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject current;
            try {
                current = array.getJSONObject(i);
                order_devices.add(order_deviceService.createOrder_device(
                                orderId,
                                Long.valueOf(current.getString("id")),
                                current.getLong("amount")
                        )
                );
            } catch (JSONException e) {
                return Mono.error(() -> new CustomException("Incorrect JSON"));
            }
        }
        return order_deviceService.saveAll(order_devices);
    }


//    /**
//     * Получение всех Заказов (DTO) по Пользователю
//     * @param request request, который должен содержать jwToken
//     */
//    public List<OrderDTO> getAllOrdersDTOByUser(HttpServletRequest request) {
//
//        //Получение telephoneNumber текущего пользователя по токену
//        String email = jwTokenService.
//                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
//        UserEntity user = userService.findByTelephoneNumber(email);
//
//        List<Order> orders = orderRepository.findAllByUserId(user);
//        List<OrderDTO> list = OrderDTO.createList(orders);
//        //Сортировка листа по дате создания
//        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));
//
//        return list;
//    }
//
//
//    /** Получение абсолютно всех Заказов (DTO) */
//    public List<OrderDTO> getAllOrdersDTO(){
//        List<Order> orders = orderRepository.findAll();
//        List<OrderDTO> list = OrderDTO.createList(orders);
//        //Сортировка листа по дате создания
//        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));
//
//        return list;
//    }
//
//
//    /**
//     * Изменение Статуса заказа
//     * @param body [json] содержит:
//     *             id - :id Заказа
//     *             status - новый статус Заказа (ACTIVE/INACTIVE)
//     * @code 201 - Created
//     * @code 400 - Incorrect JSON
//     * @code 400 - Order with this :id doesn't exist
//     * @code 400 - Incorrect status
//     */
//    public void changeStatusOfOrder(String body) {
//
//        String id = StaticMethods.parsingJson(body, "id");
//        String status = StaticMethods.parsingJson(body, "status");
//        if(id == null || status == null)
//            return;
//
//        Optional<Order> optionalOrder = orderRepository.findById(Long.valueOf(id));
//        Order order = optionalOrder.orElse(null);
//        if(order == null){
//            StaticMethods.createResponse(400, "Order with this :id doesn't exist");
//            return;
//        }
//
//        EStatusOfOrder eStatus;
//        try {
//             eStatus= EStatusOfOrder.valueOf(status);
//        }catch (IllegalArgumentException e){
//            StaticMethods.createResponse(400, "Incorrect status");
//            return;
//        }
//
//        order.setStatus(eStatus);
//        orderRepository.save(order);
//        StaticMethods.createResponse(201, "Created");
//
//    }
}
