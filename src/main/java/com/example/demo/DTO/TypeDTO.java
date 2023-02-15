//package com.example.demo.DTO;
//
//import com.example.demo.Entity.Type;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//public class TypeDTO {
//
//    @ApiModelProperty(notes = ":id Типа", name = "id", required = true, example = "13")
//    private Long id;
//
//    @ApiModelProperty(notes = "Название Типа", name = "name", required = true, example = "Смартфоны")
//    private String name;
//
//    @ApiModelProperty(notes = "Бренды, принадлежащие к данному Типу", name = "brands")
//    private List<BrandDTO> brands;
//
//    public static TypeDTO creat(Type type){
//        TypeDTO typeDTO = new TypeDTO();
//        typeDTO.setId(type.getId());
//        typeDTO.setName(type.getName());
////        List<BrandDTO> list = BrandDTO.createList(type.getBrands());
////        String[] array = new String[list.size()];
////        for(BrandDTO brandDTO: list){
////            array[list.indexOf(brandDTO)] = brandDTO.getName();
////        }
////        typeDTO.setBrandDTOS(array);
//        typeDTO.setBrands(BrandDTO.createList(type.getBrands()));
//        return typeDTO;
//    }
//
//    public static List<TypeDTO> createList(List<Type> list){
//        List<TypeDTO> newList = new ArrayList<>();
//        for(Type type: list){
//            newList.add(creat(type));
//        }
//        return newList;
//    }
//
//}
