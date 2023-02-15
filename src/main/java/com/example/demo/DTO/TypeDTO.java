package com.example.demo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
public class TypeDTO {

    @ApiModelProperty(notes = ":id Типа", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Типа", name = "name", required = true, example = "Смартфоны")
    private String name;

    @ApiModelProperty(notes = "Бренды, принадлежащие к данному Типу", name = "brands")
    private List<BrandDTO> brands;

}
