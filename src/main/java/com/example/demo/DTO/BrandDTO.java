package com.example.demo.DTO;

import com.example.demo.Entity.Brand;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BrandDTO {

    @ApiModelProperty(notes = ":id Бренда", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Название Бренда", name = "name", required = true, example = "Apple")
    private String name;

    public static BrandDTO create(Brand brand){
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId());
        brandDTO.setName(brand.getName());
        return brandDTO;
    }

    public static List<BrandDTO> createList(List<Brand> list){
        List<BrandDTO> newList = new ArrayList<>();
        for(Brand brand: list){
            newList.add(create(brand));
        }
        return newList;
    }

}
