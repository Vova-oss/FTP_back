package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Data
@NoArgsConstructor
@Table(name = "os_device")
public class Device {

    @Id
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Девайса", name = "name", required = true, example = "EI540-A Чёрный")
    @Column(value = "name")
    private String name;


    @ApiModelProperty(notes = "Цена Девайса", name = "price", required = true, example = "500")
    @Column(value = "price")
    @PositiveOrZero(message = "Некорректная цена девайса")
    @NotNull(message = "Некорректная цена девайса")
    private String price;

    @ApiModelProperty(notes = "Путь до картинки", name = "pathFile", required = true,
            example = "3e9f88a8-6f12-4683-88c8-c6e6d3efc593fff.png")
    @Column(value = "path_file")
    private String pathFile;


    @ApiModelProperty(notes = "Поле, отвечающее на вопрос:Является ли pathFile именем картинки?(Если нет, значит эта " +
            "прямая ссылка из инета)", name = "isName", required = true, example = "true")
    @Column(value = "is_name")
    private Boolean isName;

    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Девайс", name = "typeId", required = true)
    @Column(value = "type_id")
    private Long typeId;

    @ApiModelProperty(notes = "Бренд, к которому принадлежит данный Девайс", name = "brandId", required = true)
    @Column(value = "brand_id")
    private Long brandId;

    @ApiModelProperty(notes = "Дата создания Девайса", name = "dataOfCreate", required = true)
    @Column(value = "data_of_create")
    @DateTimeFormat
    private Long dataOfCreate;

}
