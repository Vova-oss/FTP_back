package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "os_brand")
public class Brand {

    @Id
    @ApiModelProperty(notes = ":id Бренда", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Бренда", name = "name", required = true, example = "Apple")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Бренд", name = "typeId", required = true)
    @Column(name = "type_id")
    private Long typeId;

}
