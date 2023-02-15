package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "os_brand")
public class Brand {

    @Id
    @ApiModelProperty(notes = ":id Бренда", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Бренда", name = "name", required = true, example = "Apple")
    @Column(value = "name")
    private String name;

    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Бренд", name = "typeId", required = true)
    @Column(value = "type_id")
    private Long typeId;

}
