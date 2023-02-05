package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_seq")
    @SequenceGenerator(name = "brand_seq", initialValue = 214, allocationSize = 1)
    @ApiModelProperty(notes = ":id Бренда", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Бренда", name = "name", required = true, example = "Apple")
    @Column(name = "name")
    private String name;


    @ApiModelProperty(notes = "Девайсы, которые принадлежат данному бренду", name = "devices")
    @JsonIgnore
    @OneToMany(mappedBy = "brandId", cascade = CascadeType.ALL)
    private List<Device> devices;


    @ApiModelProperty(notes = "Тип, к которому принадлежит данный Бренд", name = "typeId", required = true)
    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type typeId;

}
