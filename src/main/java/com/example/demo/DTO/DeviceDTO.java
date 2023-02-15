//package com.example.demo.DTO;
//
//import com.example.demo.Entity.Device;
//import com.example.demo.Entity.Rating;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//public class DeviceDTO {
//
//    @ApiModelProperty(notes = ":id Девайса", name = "id", required = true, example = "13")
//    private Long id;
//
//    @ApiModelProperty(notes = "Название Девайса", name = "name", required = true, example = "EI540-A Чёрный")
//    private String name;
//
//    @ApiModelProperty(notes = "Цена Девайса", name = "price", required = true, example = "500")
//    private String price;
//
//    @ApiModelProperty(notes = "Путь до картинки", name = "pathFile", required = true,
//            example = "3e9f88a8-6f12-4683-88c8-c6e6d3efc593fff.png")
//    private String pathFile;
//
//    @ApiModelProperty(notes = "Поле, отвечающее на вопрос:Является ли pathFile именем картинки?(Если нет, значит эта " +
//            "прямая ссылка из инета)", name = "isName", required = true, example = "true")
//    private Boolean isName;
//
//    @ApiModelProperty(notes = "Название Типа, к которому принадлежит данный Девайс",
//            name = "typeName", required = true, example = "Смартфоны")
//    private String typeName;
//
//    @ApiModelProperty(notes = "Название Брендв, к которому принадлежит данный Девайс",
//            name = "brandName", required = true, example = "Apple")
//    private String brandName;
//
//    @ApiModelProperty(notes = "Рейтинг данного девайса", name = "ratings", example = "4.9")
//    private double ratings;
//
//    @ApiModelProperty(notes = "Дополнительная инфа о Девайсе", name = "device_infoResponseModels")
//    private List<Device_infoDTO> device_infoResponseModels;
//
//    public static DeviceDTO create(Device device){
//        DeviceDTO deviceDTO = new DeviceDTO();
//        deviceDTO.setId(device.getId());
//        deviceDTO.setName(device.getName());
//        deviceDTO.setPrice(device.getPrice());
//        deviceDTO.setPathFile(device.getPathFile());
//        deviceDTO.setIsName(device.getIsName());
//        deviceDTO.setTypeName(device.getTypeId().getName());
//        deviceDTO.setBrandName(device.getBrandId().getName());
//        deviceDTO.setRatings(deviceDTO.createRating(device));
//        deviceDTO.setDevice_infoResponseModels(Device_infoDTO.createList(device.getDeviceInfos()));
//        return deviceDTO;
//    }
//
//    public static List<DeviceDTO> createList(List<Device> list){
//        List<DeviceDTO> newList = new ArrayList<>();
//        for(Device device: list){
//            newList.add(create(device));
//        }
//        return newList;
//    }
//
//    private double createRating(Device device){
//
//        double commonRating = 0;
//        for(Rating rating :device.getRatings()){
//            commonRating += Double.parseDouble(rating.getRate());
//        }
//        return commonRating;
//    }
//
//}
