package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "os_device_info")
public class Device_info {

    @Id
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Заголовок", name = "title", required = true, example = "Ширина")
    @Column(name = "title")
    private String title;


    @ApiModelProperty(notes = "Описание", name = "description", required = true, example = "2 метра")
    @Column(name = "description")
    private String description;


    @ApiModelProperty(notes = "Девайс, к которому идёт описание", name = "device", required = true)
    @Column(name = "device_id")
    private Long deviceId;

}
