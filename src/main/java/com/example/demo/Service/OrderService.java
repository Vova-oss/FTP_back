package com.example.demo.Service;


import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.DTO.OrderDTO;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Enum.EStatusOfOrder;
import com.example.demo.Entity.Order;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repositories.OrderRepository;
import com.example.demo.Security.Service.JWTokenService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.SecurityConstants.HEADER_JWT_STRING;
import static com.example.demo.Security.SecurityConstants.TOKEN_PREFIX;

@Service
public class OrderService {

    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    Order_deviceService order_deviceService;
    @Autowired
    ValidationService validationService;

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
     * @code 469 - Incorrect validation (ValidationService.class)
     */
    public void addAnOrder(String body, HttpServletRequest request, HttpServletResponse response){

        //Получение telephoneNumber текущего пользователя по токену
        String email = jwTokenService.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity user = userService.findByTelephoneNumber(email);

        try {

            JSONObject object = new JSONObject(body);
            JSONArray array = object.getJSONArray("orderItems");
            Long totalSumCheck = object.getLong("totalSumCheck");

            Order order = createNewOrder(request, response, user, totalSumCheck);
            if(order == null)
                return;

            for(int i = 0; i< array.length(); i++){

                    JSONObject current = array.getJSONObject(i);
                    Device device = deviceService.getById(current.getString("id"), request, response);
                    if(device==null){
                        continue;
                    }
                    order_deviceService.save(order, device, current.getLong("amount"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            StaticMethods.createResponse(request, response, 400, "Incorrect JSON");
            return;
        }
        StaticMethods.createResponse(request, response, 201, "Created");
    }


    /**
     * Создание новой записи о Заказе в БД
     * @param user Пользователь, которому принадлежит данный Заказ
     * @param totalSumCheck общая сумма Заказа
     * @return созданный Заказ в БД (с :id)
     *
     * @code 469 - Incorrect validation (ValidationService.class)
     */
    public Order createNewOrder(HttpServletRequest request,
                                HttpServletResponse response,
                                UserEntity user,
                                Long totalSumCheck){
        Order order = new Order();
        order.setUser(user);
        order.setStatus(EStatusOfOrder.ACTIVE);
        order.setTotalSumCheck(totalSumCheck);
        order.setDataOfCreate(System.currentTimeMillis());
        if(!validationService.validation(request, response, user))
            return null;
        return orderRepository.save(order);
    }


    /**
     * Получение всех Заказов (DTO) по Пользователю
     * @param request request, который должен содержать jwToken
     */
    public List<OrderDTO> getAllOrdersDTOByUser(HttpServletRequest request) {

        //Получение telephoneNumber текущего пользователя по токену
        String email = jwTokenService.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity user = userService.findByTelephoneNumber(email);

        List<Order> orders = orderRepository.findAllByUser(user);
        List<OrderDTO> list = OrderDTO.createList(orders);
        //Сортировка листа по дате создания
        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));

        return list;
    }


    /** Получение абсолютно всех Заказов (DTO) */
    public List<OrderDTO> getAllOrdersDTO(){
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> list = OrderDTO.createList(orders);
        //Сортировка листа по дате создания
        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));

        return list;
    }


    /**
     * Изменение Статуса заказа
     * @param body [json] содержит:
     *             id - :id Заказа
     *             status - новый статус Заказа (ACTIVE/INACTIVE)
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 432 - Order with this :id doesn't exist
     * @code 433 - Incorrect status
     */
    public void changeStatusOfOrder(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        String status = StaticMethods.parsingJson(body, "status", request, response);
        if(id == null || status == null)
            return;

        Optional<Order> optionalOrder = orderRepository.findById(Long.valueOf(id));
        Order order = optionalOrder.orElse(null);
        if(order == null){
            StaticMethods.createResponse(request, response, 432, "Order with this :id doesn't exist");
            return;
        }

        EStatusOfOrder eStatus;
        try {
             eStatus= EStatusOfOrder.valueOf(status);
        }catch (IllegalArgumentException e){
            StaticMethods.createResponse(request, response, 433, "Incorrect status");
            return;
        }

        order.setStatus(eStatus);
        orderRepository.save(order);
        StaticMethods.createResponse(request, response, 201, "Created");

    }
}
