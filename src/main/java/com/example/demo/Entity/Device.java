package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_device", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Device extends BaseEntity{

    @ApiModelProperty(notes = "Название Девайса", name = "name", required = true, example = "EI540-A Чёрный")
    @Column(name = "name")
    private String name;


    @ApiModelProperty(notes = "Цена Девайса", name = "price", required = true, example = "500")
    @Column(name = "price")
    @PositiveOrZero(message = "Некорректная цена девайса")
    @NotNull(message = "Некорректная цена девайса")
    private String price;


    @ApiModelProperty(notes = "Путь до картинки", name = "pathFile", required = true,
            example = "3e9f88a8-6f12-4683-88c8-c6e6d3efc593fff.png")
    @Column(name = "path_file", columnDefinition = "TEXT")
    private String pathFile;


    @ApiModelProperty(notes = "Поле, отвечающее на вопрос:Является ли pathFile именем картинки?(Если нет, значит эта " +
            "прямая ссылка из инета)", name = "isName", required = true, example = "true")
    @Column(name = "is_name")
    private Boolean isName;


    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Девайс", name = "typeId", required = true)
    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type typeId;


    @ApiModelProperty(notes = "Бренд, к которому принадлежит данный Девайс", name = "brandId", required = true)
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brandId;


    @ApiModelProperty(notes = "Рейтинг данного девайса", name = "ratings")
    @OneToMany(mappedBy = "device")
    private List<Rating> ratings;


    @ApiModelProperty(notes = "Дополнительная инфа о Девайсе", name = "deviceInfos")
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<Device_info> deviceInfos;


    @ApiModelProperty(notes = "Строки заказов, к которым принадлежит данный Девайс", name = "order_device")
    @OneToOne(mappedBy = "device")
    private Order_device order_device;


    @ApiModelProperty(notes = "Дата создания Девайса", name = "dataOfCreate", required = true)
    @Column(name = "data_of_create")
    @DateTimeFormat
    private Long dataOfCreate;

}
