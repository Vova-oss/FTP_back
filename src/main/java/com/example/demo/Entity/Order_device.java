package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
    @Column(value = "order_id")
    private Long orderId;


    @ApiModelProperty(notes = "Девайс в заказе", name = "device", required = true)
    @Column(value = "device_id")
    private Long deviceId;


    @ApiModelProperty(notes = "Количество данного Девайса в заказе", name = "amountOfProduct",
            required = true, example = "7")
    @Positive(message = "Некорректное количество продуктов")
    @NotNull(message = "Некорректное количество продуктов")
    @Column(value = "amount_of_product")
    private Long amountOfProduct;

}
