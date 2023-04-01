package com.example.demo.Kafka.Message;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderMessage {

    private Long id;
    private Long userId;
    private String status;
    private Long totalSumCheck;
    private Long dataOfCreate;
    private List<DeviceInOrder> devices;

}
