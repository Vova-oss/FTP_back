package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@Table(name = "os_order_device")
public class Order_device {

    @Id
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Ссылка на Заказ", name = "order", required = true)
    @Column(name = "order_id")
    private Long order_id;


    @ApiModelProperty(notes = "Девайс в заказе", name = "device", required = true)
    @Column(name = "device_id")
    private Long device_id;


    @ApiModelProperty(notes = "Количество данного Девайса в заказе", name = "amountOfProduct",
            required = true, example = "7")
    @Positive(message = "Некорректное количество продуктов")
    @NotNull(message = "Некорректное количество продуктов")
    @Column(name = "amount_of_product")
    private Long amountOfProduct;

}
