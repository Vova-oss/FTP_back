package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_order_device")
public class Order_device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Ссылка на Заказ", name = "order", required = true)
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "order_id")
    private Order order;


    @ApiModelProperty(notes = "Девайс в заказе", name = "device", required = true)
    @OneToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "device_id")
    private Device device;


    @ApiModelProperty(notes = "Количество данного Девайса в заказе", name = "amountOfProduct",
            required = true, example = "7")
    @Positive(message = "Некорректное количество продуктов")
    @NotNull(message = "Некорректное количество продуктов")
    @Column(name = "amount_of_product")
    private Long amountOfProduct;

}
