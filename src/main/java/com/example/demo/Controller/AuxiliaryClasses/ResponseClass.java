package com.example.demo.Controller.AuxiliaryClasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ApiModel(description = "Класс для визуализации response-ошибок")
public class ResponseClass implements Serializable {


    @ApiModelProperty(name = "status", value = "Status code",example = "400")
    private int status;

    @ApiModelProperty(name = "info", value = "Information about exception", example = "User with this telephoneNumber already exist")
    private String info;

    public ResponseClass(int status, String info) {
        this.status = status;
        this.info = info;
    }

    @Override
    public String toString() {
        return "{" +
                "\"status\":" + status + "," +
                "\"info\":\"" + info + "\"" +
                '}';
    }
}
