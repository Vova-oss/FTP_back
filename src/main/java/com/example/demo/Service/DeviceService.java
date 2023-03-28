package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.DeviceDTOList;
import com.example.demo.DTO.Device_infoDTO;
import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Device_info;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {


    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    Device_infoRepository device_infoRepository;
    @Autowired
    DeviceDTORepo deviceDTORepo;
    @Autowired
    DeviceDTOListRepo deviceDTOListRepo;

    /**
     * Добавление Девайса
     *
     * @param brandName название Бренда, к которому будет относится Девайс
     * @param typeName название Типа, к которому будет относится Девайс
     * @param file картинка в битовом представление (ава Девайса)
     * @param ref ссылка на картинку (ава Девайса)
     * @param name название Девайса
     * @param price цена Девайса
     * @param list лист с характеристиками Девайса (Device_info)
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - This name of Device already exists
     * @code 400 - This Type doesn't exist
     * @code 400 - This Brand (%s) of this Type (%s) doesn't exist
     * @code 400 - Incorrect image extension
     */
    public Mono<Void> addDevice(String id,
                          String brandName,
                          String typeName,
                          MultipartFile file,
                          String ref,
                          String name,
                          String price,
                          String list) {
        return typeRepository
                .findByName(typeName)
                .switchIfEmpty(Mono.error(new CustomException("This Type doesn't exist")))
                .zipWhen(type -> {
                    return brandRepository
                            .findByNameAndTypeId(brandName, type.getId())
                            .switchIfEmpty(Mono.error(new CustomException(
                                    String.format("This Brand (%s) of this Type (%s) doesn't exist", brandName, typeName)))
                            );
                })
                .flatMap(tuple2 -> {
                    Type type = tuple2.getT1();
                    Brand brand = tuple2.getT2();
                    Device device = createBodyOfDevice(id, brand.getId(), type.getId(), file, ref, name, price);

                    if(id != null)
                        return deviceRepository
                                .findById(Long.valueOf(id))
                                .switchIfEmpty(Mono.error(new CustomException("Device with this :id doesn't exist")))
                                .flatMap(dFromDB -> deviceRepository.save(device))
                                .onErrorResume(throwable -> Mono.error(new CustomException("This name of Device already exists")))
                                .flatMap(savedDevice -> createDeviceInfoAndSaveInDB(list, device.getId()));

                    else return deviceRepository
                            .save(device)
                            .onErrorResume(throwable -> Mono.error(new CustomException("This name of Device already exists")))
                            .flatMap(savedDevice -> createDeviceInfoAndSaveInDB(list, device.getId()));
                })
                .then(Mono.empty());
    }

    @SneakyThrows
    private Device createBodyOfDevice(String id,
                                      Long brandId,
                                      Long typeId,
                                      MultipartFile file,
                                      String ref,
                                      String name,
                                      String price){
        Device device = new Device();
        if(id != null)
            device.setId(Long.valueOf(id));

        if(file != null) {
            String startName = StaticMethods.getFileExtension(file.getOriginalFilename());
            if(startName==null || (
                    !startName.equals("jpeg")
                            && !startName.equals("jpg")
                            && !startName.equals("pjpeg")
                            && !startName.equals("png")
                            && !startName.equals("tiff")
                            && !startName.equals("wbmp")
                            && !startName.equals("webp"))
            ){
                throw new CustomException("Incorrect image extension");
            }

            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + file.getOriginalFilename();
            String pathFile = System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + fileName;


            try {
                file.transferTo(new File(pathFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            device.setPathFile(fileName);
            device.setIsName(true);
        }else if(ref!=null) {
            device.setPathFile(ref);
            device.setIsName(false);
        }

        device.setTypeId(typeId);
        device.setBrandId(brandId);
        device.setName(name);
        device.setPrice(price);
        device.setDataOfCreate(System.currentTimeMillis());
        return device;
    }

    /**
     * Добавление дополнительно информации Девайсу с последующим сохранением в БД
     * @param strList лист с характеристиками Девайса (Device_info)
     * @param deviceId :id Девайса, которому добавляют Device_info
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     */
    @SneakyThrows
    public Mono<Void> createDeviceInfoAndSaveInDB(String strList, Long deviceId){
        JSONArray list;
        try {
            list = new JSONArray(strList);
        } catch (JSONException e) {
            throw new CustomException("Incorrect JSON in characteristics");
        }

        List<Device_info> device_infos = new ArrayList<>();

        for(int i = 0; i < list.length(); i++){
            String characteristic = list.getString(i);
            Device_info device_info;
            try{
                device_info = new ObjectMapper().readValue(characteristic, Device_info.class);
            } catch (JsonProcessingException e ){
                e.printStackTrace();
                throw new CustomException("Incorrect JSON");
            }
            device_info.setDeviceId(deviceId);
            device_infos.add(device_info);
        }

        return device_infoRepository
                .saveAll(device_infos)
                .then(Mono.empty());
    }


    /**
     * Получение Девайсов исходя из входных параметров
     * @param typeName Название Типа, к которому относится Девайс
     * @param brands Название Бренда, к которому относится Девайс
     * @param page Номер страницы, которую хочет видеть клиент
     * @param limit Количество Девайсов, отображаемых на одной странице у клиента
     * @param minPrice минимальная цена Девайса
     * @param maxPrice максимальная цена Девайса
     *
     * @code = 400 - The minPrice more than the maxPrice
     * @code = 400 - Devices with this type doesn't exists
     * @code = 400 - Devices with this brands doesn't exists
     * @code = 400 - Devices with this price doesn't exists
     * @code = 400 - Incorrect data of page or limit
     */
    public Mono<DeviceDTOList> getByParams(String typeName,
                                                           List<String> brands,
                                                           int page, int limit,
                                                           int minPrice, int maxPrice) {

        if(maxPrice!=-1 && minPrice > maxPrice)
            return Mono.error(new CustomException("The minPrice more than the maxPrice"));


        int offset = (page - 1) * limit;

//         Условие для получения листа Девайсов исходя из переданных Брендов(если они имеются) и Типа
        if(brands==null || brands.size()==0){
            return deviceDTORepo
                    .findAllWithParamsWithoutBrands(typeName, minPrice, maxPrice, offset, limit)
                    .switchIfEmpty(Mono.error(new CustomException("Devices with this params doesn't exists")))
                    .publishOn(Schedulers.boundedElastic())
                    .map(deviceDTO -> {
                        device_infoRepository
                                .findAllByDeviceId(deviceDTO.getId())
                                .collectList()
                                .map(Device_infoDTO::createList)
                                .map(device_infoDTOS -> {
                                    deviceDTO.setDevice_infoResponseModels(device_infoDTOS);
                                    return device_infoDTOS;
                                })
                                .block();
                        return deviceDTO;
                    })
                    .collectList()
                    .map(deviceDTOS -> {
                        DeviceDTOList deviceDTOList = new DeviceDTOList();
                        deviceDTOList.setList(deviceDTOS);

                        deviceDTOListRepo
                                .findByParamsWithoutBrands(typeName)
                                .map(allInfo ->{
                                    deviceDTOList.setAmountOfAllDevices(allInfo.getAmountOfAllDevices());
                                    deviceDTOList.setMaxPrice(allInfo.getMaxPrice());
                                    deviceDTOList.setMinPrice(allInfo.getMinPrice());
                                    return allInfo;
                                })
                                .block();

                        return deviceDTOList;
                    });
        }else {
            return deviceDTORepo
                    .findAllWithParamsWithBrands(typeName, brands, minPrice, maxPrice, offset, limit)
                    .switchIfEmpty(Mono.error(new CustomException("Devices with this params doesn't exists")))
                    .publishOn(Schedulers.boundedElastic())
                    .map(deviceDTO -> {
                        device_infoRepository
                                .findAllByDeviceId(deviceDTO.getId())
                                .collectList()
                                .map(Device_infoDTO::createList)
                                .map(device_infoDTOS -> {
                                    deviceDTO.setDevice_infoResponseModels(device_infoDTOS);
                                    return device_infoDTOS;
                                })
                                .block();
                        return deviceDTO;
                    })
                    .collectList()
                    .map(deviceDTOS -> {
                        DeviceDTOList deviceDTOList = new DeviceDTOList();
                        deviceDTOList.setList(deviceDTOS);

                        deviceDTOListRepo
                                .findByParamsWithBrands(typeName, brands)
                                .map(allInfo ->{
                                    deviceDTOList.setAmountOfAllDevices(allInfo.getAmountOfAllDevices());
                                    deviceDTOList.setMaxPrice(allInfo.getMaxPrice());
                                    deviceDTOList.setMinPrice(allInfo.getMinPrice());
                                    return allInfo;
                                })
                                .block();

                        return deviceDTOList;
                    });
        }
    }


//    /**
//     * Фильтрация листа с Девайсами по цене
//     * @param list лист Девайсов, который будет отфильтрован, исходя из мин. и макс. ценовых значений
//     * @param minPrice минимальная цена Девайса
//     * @param maxPrice максимальная цена Девайса
//     */
//    public List<Device> selectionByPrice(List<Device> list, int minPrice, int maxPrice){
//
//        if(maxPrice == -1){
//
//            return list.
//                    stream().
//                    filter(device -> minPrice <= Integer.parseInt(device.getPrice())).
//                    collect(Collectors.toList());
//        }else{
//
//            return list.
//                    stream().
//                    filter(device -> minPrice <= Integer.parseInt(device.getPrice())
//                            && Integer.parseInt(device.getPrice()) <= maxPrice).
//                    collect(Collectors.toList());
//        }
//    }
//
//
//    /**
//     * Поиск всех Девайсов по Типу
//     * @param type Тип, к которому принадлежит Девайс
//     */
//    public List<Device> findAllByTypeId(Type type){
//        return deviceRepository.findAllByTypeId(type);
//    }
//
//
//    /**
//     * Поиск всех Девайсов по Бренду
//     * @param brand Бренд, к которому принадлежит Девайс
//     */
//    public List<Device> findAllByBrandId(Brand brand){
//        return deviceRepository.findAllByBrandId(brand);
//    }


    /** Получение всех Девайсов*/
    public Flux<Device> getAll() {
        return deviceRepository.findAll();
    }


    /**
     * Удаление Девайса по его :id
     * @param body (json) :id Девайса
     *
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Device with this id
     */
    public Mono<Void> deleteDevice(String body) {


        Long id = Long.valueOf(StaticMethods.parsingJson(body, "id"));
        return deviceRepository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> new CustomException("There isn't exist Device with this id")))
                .flatMap(device -> {
                    if(device.getIsName())
                        new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + device.getPathFile()).delete();
                    return device_infoRepository.deleteAllByDeviceId(id);
                })
                .then(deviceRepository.deleteById(id));
    }


    /**
     * Получение Девайса и дальнейшее его конвертирование в DeviceDTO
     * @param id :id Девайса
     * @code 400 - There isn't exist Device with this id
     */
    public Mono<DeviceDTO> getDTOById(String id) {

        return deviceDTORepo
                .getById(Long.valueOf(id))
                .publishOn(Schedulers.boundedElastic())
                .map(deviceDTO -> {
                    device_infoRepository
                            .findAllByDeviceId(Long.valueOf(id))
                            .collectList()
                            .map(Device_infoDTO::createList)
                            .map(device_infoDTOS -> {
                                deviceDTO.setDevice_infoResponseModels(device_infoDTOS);
                                return device_infoDTOS;
                            })
                            .block();
                    return deviceDTO;
                });
    }

//    /**
//     * Получение Девайса по :id
//     * @param id :id Девайса
//     * @code 400 - There isn't exist Device with this id
//     */
//    public Device getById(String id){
//        Device device = deviceRepository.findById(Long.valueOf(id)).orElse(null);
//        if(device==null){
//            StaticMethods.createResponse(400,"There isn't exist Device with this id");
//            return null;
//        }
//        return device;
//    }


    /**
     * Метод для изменение Девайса по :id
     * @param id :id существующего Девайса, который желаем изменить
     * @param brand название Бренда, к которому будет относится Девайс
     * @param type название Типа, к которому будет относится Девайс
     * @param file картинка в битовом представление (ава Девайса)
     * @param ref ссылка на картинку (ава Девайса)
     * @param name название Девайса
     * @param price цена Девайса
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - This name of Device already exists
     * @code 400 - This Type doesn't exist
     * @code 400 - This Brand (%s) of this Type (%s) doesn't exist
     * @code 400 - Incorrect image extension
     * @code 400 - Device with this :id doesn't exist
     */
    public Mono<Void> editDevice(String id,
                           String brand,
                           String type,
                           String ref,
                           MultipartFile file,
                           String name,
                           String price,
                           String list) {

        return addDevice(id, brand, type, file, ref, name, price, list);
    }

    public Flux<DeviceDTO> getTopDevices() {

       return deviceDTORepo
                .findTop24()
               .publishOn(Schedulers.boundedElastic())
                .map(deviceDTO -> {
                    device_infoRepository
                            .findAllByDeviceId(deviceDTO.getId())
                            .collectList()
                            .map(Device_infoDTO::createList)
                            .map(device_infoDTOS -> {
                                deviceDTO.setDevice_infoResponseModels(device_infoDTOS);
                                return device_infoDTOS;
                            })
                            .block();
                    return deviceDTO;
                });
    }
}
