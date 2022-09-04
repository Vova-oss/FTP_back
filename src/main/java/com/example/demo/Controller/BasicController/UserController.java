package com.example.demo.Controller.BasicController;

import com.example.demo.Entity.Response.ResponseClass;
import com.example.demo.Service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(tags = "User")
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @ApiOperation(value = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 432, message = "User with this telephoneNumber already exist"),
            @ApiResponse(code = 469, message = "Json-формат со следующими полями:\nfield - лист полей, к которым " +
                    "относятся ошибки\ninfo - характеристика каждой ошибки")
    })
    @PostMapping("/registration")
    public void registration(
            @ApiParam(type = "String",
                      value = "ФИО, Телефонный номер и пароль",
                      example = "{\n\"FIO\":\"Полетаев Владимир Викторович\", " +
                              "\n\"telephoneNumber\": \"+79645932177\",\n\"password\": \"password\"}",
                      required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        userService.addUser(body, request, response);
    }

    @ApiOperation(value = "Подтверждение кода")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Code is confirmed"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 432, message = "User with this :telephoneNumber doesn't exist"),
            @ApiResponse(code = 433, message = "Incorrect code"),
            @ApiResponse(code = 434, message = "Code has already been confirmed")
    })
//    @CrossOrigin("*")
    @PutMapping("/codeConfirmation")
    public void codeConfirmation(
            @ApiParam(type = "String",
                    value = "Телефонный номер и код подтверждения",
                    example = "{\n\"telephoneNumber\": \"+79645932177\",\n\"code\": \"468175\"}",
                    required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){

        userService.codeConfirmation(body, request, response);

    }

    @ApiOperation(value = "Отправка кода по указанному номеру для восстановления пароля")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Code has been sent"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 432, message = "User with this :telephoneNumber doesn't exist"),
    })
    @PutMapping("/sendSMSForPasswordRecovery")
    public void sendSMSForPasswordRecovery(
            @ApiParam(type = "String",
                    value = "Телефонный номер",
                    example = "{\n\"telephoneNumber\": \"+79645932177\"}",
                    required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){

        userService.sendSMSForPasswordRecovery(body, request, response);

    }

    @ApiOperation(value = "Изменение пароля")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Password have changed"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 432, message = "User with this :telephoneNumber doesn't exist"),
    })
    @PutMapping("/changePassword")
    public void changePassword(
            @ApiParam(type = "String",
                    value = "Телефонный номер и новый пароль",
                    example = "{\n\"telephoneNumber\": \"+79645932177\",\n\"password\": \"newPassword\"}",
                    required = true)
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        userService.changePassword(body, request, response);
    }

    @ApiOperation(value = "Проверка роли пользователя (Необходим JWT в хедере запроса)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Incorrect JWT token")
    })
    @GetMapping("/checkRole")
    public ResponseEntity<ResponseClass> checkRole(HttpServletRequest request){
        return userService.checkRole(request);
    }


    @ApiOperation(value = "Удаление пользователя по telephoneNumber", hidden = true)
    @DeleteMapping("/deleteByTelephoneNumber")
    public void delete(@RequestBody String telephoneNumber){
        userService.deleteByTelephoneNumber(telephoneNumber);
    }

}
