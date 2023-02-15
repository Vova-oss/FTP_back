package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "os_device_info")
public class Device_info {

    @Id
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Заголовок", name = "title", required = true, example = "Ширина")
    @Column(value = "title")
    private String title;


    @ApiModelProperty(notes = "Описание", name = "description", required = true, example = "2 метра")
    @Column(value = "description")
    private String description;


    @ApiModelProperty(notes = "Девайс, к которому идёт описание", name = "device", required = true)
    @Column(value = "device_id")
    private Long deviceId;

}
