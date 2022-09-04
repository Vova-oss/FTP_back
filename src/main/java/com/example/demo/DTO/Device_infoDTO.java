package com.example.demo.DTO;

import com.example.demo.Entity.Device_info;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Device_infoDTO {

    @ApiModelProperty(notes = ":id Информации о девайсе", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Заголовок", name = "title", required = true, example = "Ширина")
    private String title;

    @ApiModelProperty(notes = "Описание", name = "description", required = true, example = "2 метра")
    private String description;

    public static Device_infoDTO create(Device_info device_info){
        Device_infoDTO device_infoDTO = new Device_infoDTO();
        device_infoDTO.setId(device_info.getId());
        device_infoDTO.setTitle(device_info.getTitle());
        device_infoDTO.setDescription(device_info.getDescription());
        return device_infoDTO;
    }

    public static List<Device_infoDTO> createList(List<Device_info> list){
        List<Device_infoDTO> newList = new ArrayList<>();
        for(Device_info device_info: list){
            newList.add(create(device_info));
        }
        return newList;
    }

}
