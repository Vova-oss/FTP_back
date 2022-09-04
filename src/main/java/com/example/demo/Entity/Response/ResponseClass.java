package com.example.demo.Entity.Response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Data
@ApiModel(description = "Класс для визуализации response-ошибок")
public class ResponseClass implements Serializable {


    @ApiModelProperty(name = "status", value = "400",example = "200")
    private int status;

    @ApiModelProperty(name = "info", value = "User with this telephoneNumber already exist", example = "ADMIN")
    private String info;

    @ApiModelProperty(name = "path", value = "/user/registration", example = "/user/checkAdminRole")
    private String path;



    public ResponseClass(int status, String info, String path) {
        this.status = status;
        this.info = info;
        this.path = path;
    }
}
