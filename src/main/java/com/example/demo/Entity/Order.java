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
@Table(name = "os_order")
public class Order {

    @Id
    @ApiModelProperty(notes = ":id пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Пользователь, к которому относится заказ", name = "user", required = true)
    @Column(value = "user_id")
    private Long userId;

    @ApiModelProperty(notes = "Статус заказа (ACTIVE/INACTIVE)", name = "status", required = true, example = "ACTIVE")
    private String status;

    @ApiModelProperty(notes = "Общая сумма заказа", name = "totalSumCheck", required = true, example = "1950")
    @Column(value = "total_sum_check")
    @PositiveOrZero(message = "Некорректная итоговая сумма")
    @NotNull(message = "Некорректная итоговая сумма")
    private Long totalSumCheck;

    @ApiModelProperty(notes = "Дата создания заказа", name = "dataOfCreate", required = true)
    @Column(value = "data_of_create")
    @DateTimeFormat
    private Long dataOfCreate;

}
