package com.example.demo.Entity;

import com.example.demo.Entity.Enum.EStatusOfOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Пользователь, к которому относится заказ", name = "user", required = true)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @ApiModelProperty(notes = "Ссылки на order-devices", name = "order_devices", required = true)
    @OneToMany(mappedBy = "order")
    private List<Order_device> order_devices;


    @ApiModelProperty(notes = "Статус заказа (ACTIVE/INACTIVE)", name = "status", required = true, example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private EStatusOfOrder status;


    @ApiModelProperty(notes = "Общая сумма заказа", name = "totalSumCheck", required = true, example = "1950")
    @Column(name = "total_sum_check")
    @PositiveOrZero(message = "Некорректная итоговая сумма")
    @NotNull(message = "Некорректная итоговая сумма")
    private Long totalSumCheck;


    @ApiModelProperty(notes = "Дата создания заказа", name = "dataOfCreate", required = true)
    @Column(name = "data_of_create")
    @DateTimeFormat
    private Long dataOfCreate;

}
