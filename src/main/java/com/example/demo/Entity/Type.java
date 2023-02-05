package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_type",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_seq")
    @SequenceGenerator(name = "type_seq", initialValue = 8, allocationSize = 1)
    @ApiModelProperty(notes = ":id Типа", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Типа", name = "name", required = true, example = "Смартфоны")
    @Column(name = "name")
    private String name;


    @ApiModelProperty(notes = "Девайсы, принадлежащие к данному Типу", name = "devices")
    @JsonIgnore
    @OneToMany(mappedBy = "typeId", cascade = CascadeType.ALL)
    private List<Device> devices;


    @ApiModelProperty(notes = "Бренды, принадлежащие к данному Типу", name = "brands")
    @OneToMany(mappedBy = "typeId", cascade = CascadeType.ALL)
    private List<Brand> brands;

}
