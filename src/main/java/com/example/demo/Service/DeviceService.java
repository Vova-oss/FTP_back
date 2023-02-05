package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.DTO.DeviceDTO;
import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Device_info;
import com.example.demo.Entity.Response.DeviceWIthNecessaryParameters;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {


    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    BrandService brandService;
    @Autowired
    TypeService typeService;
    @Autowired
    Device_infoService device_infoService;


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
    public void addDevice(String brandName,
                          String typeName,
                          MultipartFile file,
                          String ref,
                          String name,
                          String price,
                          JSONArray list) {

        Device device = new Device();

        createDeviceAndSaveInDB(device, brandName, typeName, file, ref, name, price);
        createDeviceInfoAndSaveInDB(list, device);
    }

    /**
     * Сохранения Девайса в БД с добавлением ему переданных значений
     * @param device Девайс, которому добавляют все значения
     * @param brandName название Бренда, к которому будет относится Девайс
     * @param typeName название Типа, к которому будет относится Девайс
     * @param file картинка в битовом представление (ава Девайса)
     * @param ref ссылка на картинку (ава Девайса)
     * @param name название Девайса
     * @param price цена Девайса
     *
     * @code 400 - This name of Device already exists
     * @code 400 - This Type doesn't exist
     * @code 400 - This Brand (%s) of this Type (%s) doesn't exist
     * @code 400 - Incorrect image extension
     */
    public void createDeviceAndSaveInDB(Device device,
                                        String brandName,
                                        String typeName,
                                        MultipartFile file,
                                        String ref,
                                        String name,
                                        String price){
        if(deviceRepository.existsByName(name)){
            Device temp = deviceRepository.findById(device.getId()).orElse(null);
            if(temp == null || !temp.getName().equals(name)){
                StaticMethods.createResponse(400, "This name of Device already exists");
                return;
            }

        }
        Type type = typeService.findByName(typeName);
        if(type == null){
            StaticMethods.createResponse(400, "This Type doesn't exist");
            return;
        }
        Brand brand = brandService.findByNameAndTypeId(brandName, type);
        if(brand == null){
            StaticMethods.createResponse(400,
                    String.format(
                            "This Brand (%s) of this Type (%s) doesn't exist",
                            typeName,
                            brandName));
            return;
        }



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
                StaticMethods.createResponse(400,
                        "Incorrect image extension");
                return;
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

        device.setTypeId(type);
        device.setBrandId(brand);
        device.setName(name);
        device.setPrice(price);
        device.setDataOfCreate(System.currentTimeMillis());

        deviceRepository.save(device);
    }

    /**
     * Добавление дополнительно информации Девайсу с последующим сохранением в БД
     * @param list лист с характеристиками Девайса (Device_info)
     * @param device сам Девайс, которому добавляют Device_info
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     */
    public void createDeviceInfoAndSaveInDB(JSONArray list, Device device){
        try {

            for(int i = 0; i < list.length(); i++){
                String characteristic = list.getString(i);


                Device_info device_info;
                try{
                    device_info = new ObjectMapper().readValue(characteristic, Device_info.class);
                } catch (JsonProcessingException e ){
                    e.printStackTrace();
                    StaticMethods.createResponse( 400, "Incorrect JSON");
                    return;
                }
                device_info.setDevice(device);
                device_infoService.addDevice_info(device_info);
            }

            StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "Created");

        } catch (JSONException e) {
            StaticMethods.createResponse(HttpServletResponse.SC_BAD_REQUEST, "Incorrect JSON");
            e.printStackTrace();
        }
    }


    /**
     * Получение Девайсов исходя из входных параметров
     * @param type Название Типа, к которому относится Девайс
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
    public DeviceWIthNecessaryParameters getByParams(String type,
                                           List<String> brands,
                                           int page, int limit,
                                           int minPrice, int maxPrice) {

        if(maxPrice!=-1 && minPrice > maxPrice){
            StaticMethods.createResponse(400,"The minPrice more than the maxPrice");
            return null;
        }

        List<Device> list = new ArrayList<>();

        // Условие для получения листа Девайсов исходя из переданных Брендов(если они имеются) и Типа
        if(brands==null || brands.size()==0){
             list = deviceRepository.findAllByTypeId(typeService.findByName(type));

            if(list.isEmpty()){
                StaticMethods.createResponse(400,"Devices with this type doesn't exists");
                return null;
            }
        }else {
            brands = brands.stream().distinct().collect(Collectors.toList());
            for(String brand: brands) {

                Type typeParent = typeService.findByName(type);
                Brand brandParent = brandService.findByNameAndTypeId(brand, typeParent);
                list.addAll(deviceRepository.findAllByTypeIdAndBrandId(typeParent, brandParent));
            }

            if(list.isEmpty()){
                StaticMethods.createResponse(400,"Devices with this brands doesn't exists");
                return null;
            }
        }


        // Девайс с максимальной ценой в этом листе
        Optional<Device> deviceWithMaxPrice = list.stream().max(Comparator.comparing(o -> Integer.valueOf(o.getPrice())));

        // Девайс с минимальной ценой в этом листе
        Optional<Device> deviceWithMinPrice = list.stream().min(Comparator.comparing(o -> Integer.valueOf(o.getPrice())));

        list = selectionByPrice(list, minPrice, maxPrice);

        if(list.isEmpty()){
            StaticMethods.createResponse(400,"Devices with this price doesn't exists");
            return null;
        }

        // Сортировка по дате создания
        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));

        int amountOfAllDevices = list.size();

        // Определение границ эл. в листе исходя из количества отображаемых эл. на стр. и номера стр.
        int fromIndex = (page - 1) * limit;
        int toIndex = page * limit;
        if(fromIndex >= list.size()){
            StaticMethods.createResponse(400,"Incorrect data of page or limit");
            return null;
        }
        if(toIndex > list.size()){
            toIndex = list.size();
        }

        List<DeviceDTO> listDTO = DeviceDTO.createList(list.subList(fromIndex, toIndex));

        return new DeviceWIthNecessaryParameters(listDTO,
                amountOfAllDevices,
                deviceWithMaxPrice.orElse(null).getPrice(),
                deviceWithMinPrice.orElse(null).getPrice());
    }


    /**
     * Фильтрация листа с Девайсами по цене
     * @param list лист Девайсов, который будет отфильтрован, исходя из мин. и макс. ценовых значений
     * @param minPrice минимальная цена Девайса
     * @param maxPrice максимальная цена Девайса
     */
    public List<Device> selectionByPrice(List<Device> list, int minPrice, int maxPrice){

        if(maxPrice == -1){

            return list.
                    stream().
                    filter(device -> minPrice <= Integer.parseInt(device.getPrice())).
                    collect(Collectors.toList());
        }else{

            return list.
                    stream().
                    filter(device -> minPrice <= Integer.parseInt(device.getPrice())
                            && Integer.parseInt(device.getPrice()) <= maxPrice).
                    collect(Collectors.toList());
        }
    }


    /**
     * Поиск всех Девайсов по Типу
     * @param type Тип, к которому принадлежит Девайс
     */
    public List<Device> findAllByTypeId(Type type){
        return deviceRepository.findAllByTypeId(type);
    }


    /**
     * Поиск всех Девайсов по Бренду
     * @param brand Бренд, к которому принадлежит Девайс
     */
    public List<Device> findAllByBrandId(Brand brand){
        return deviceRepository.findAllByBrandId(brand);
    }


    /** Получение всех Девайсов*/
    public List<Device> getAll() {
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
    public void deleteDevice(String body) {

        String id = StaticMethods.parsingJson(body, "id");
        if(id == null)
            return;
        Device device = deviceRepository.findById(Long.valueOf(id)).orElse(null);

        if(device!=null && device.getIsName()){
            new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + device.getPathFile()).delete();
            deviceRepository.delete(device);
            StaticMethods.createResponse(HttpServletResponse.SC_NO_CONTENT, "No Content");
            return;
        }

        StaticMethods.createResponse(400,"There isn't exist Device with this id");
    }


    /**
     * Получение Девайса и дальнейшее его конвертирование в DeviceDTO
     * @param id :id Девайса
     * @code 400 - There isn't exist Device with this id
     */
    public DeviceDTO getDTOById(String id) {
        Device device = deviceRepository.findById(Long.valueOf(id)).orElse(null);
        if(device==null){
            StaticMethods.createResponse(400,"There isn't exist Device with this id");
            return null;
        }
        return DeviceDTO.create(device);
    }

    /**
     * Получение Девайса по :id
     * @param id :id Девайса
     * @code 400 - There isn't exist Device with this id
     */
    public Device getById(String id){
        Device device = deviceRepository.findById(Long.valueOf(id)).orElse(null);
        if(device==null){
            StaticMethods.createResponse(400,"There isn't exist Device with this id");
            return null;
        }
        return device;
    }


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
    public void editDevice(String id,
                           String brand,
                           String type,
                           String ref,
                           MultipartFile file,
                           String name,
                           String price,
                           JSONArray list) {

        Device device = deviceRepository.findById(Long.valueOf(id)).orElse(null);
        if(device == null){
            StaticMethods.createResponse(400, "Device with this :id doesn't exist");
            return;
        }

        createDeviceAndSaveInDB(device, brand, type, file, ref, name, price);
        createDeviceInfoAndSaveInDB(list, device);
    }

    public List<DeviceDTO> getTopDevices() {
        List<Device> list = deviceRepository.findAll();
        list.sort((o1, o2) -> o2.getDataOfCreate().compareTo(o1.getDataOfCreate()));
        list.subList(0, Math.min(list.size(), 24));
        return DeviceDTO.createList(list);
    }
}
