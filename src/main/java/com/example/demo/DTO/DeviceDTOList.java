package com.example.demo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDTOList {

    @ApiModelProperty(value = "Девайсы без лишних полей")
    List<DeviceDTO> list;

    @ApiModelProperty(value = "Количество всех существующих девайсов")
    int amountOfAllDevices;

    @ApiModelProperty(value = "Минимальная цена по данному запросу")
    String maxPrice;

    @ApiModelProperty(value = "Максимальная цена по данному запросу")
    String minPrice;

    public DeviceDTOList(List<DeviceDTO> list, int amountOfAllDevices, String maxPrice, String minPrice) {
        this.list = list;
        this.amountOfAllDevices = amountOfAllDevices;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }



}
