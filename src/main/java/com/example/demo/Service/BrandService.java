package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.BrandRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    TypeService typeService;
    @Autowired
    DeviceService deviceService;


    /**
     * Добавление нового Бренда
     * @param body [json] название Типа(type),
     *             которому принадлежит Бренд, и название нового Бренда(name)
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 432 - Such Brand of this Type already exist
     * @code 433 - Such Type doesn't exist
     */
    public void addBrand(String body, HttpServletRequest request, HttpServletResponse response) {
        Type type = typeService.findByName(StaticMethods.parsingJson(body, "type", request, response));

        Brand brand = new Brand();
        if(type!=null){
            try {
                JSONObject obj = new JSONObject(body);
                String name = obj.getString("name");
                brand.setName(name);
                brand.setTypeId(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<Brand> list = type.getBrands();
            for(Brand currentBrand: list){
                if(currentBrand.getName().equals(brand.getName())){
                    StaticMethods.createResponse(
                            request, response,432,
                            "Such Brand of this Type already exist");
                    return;
                }
            }

            brand.setTypeId(type);
            brandRepository.save(brand);
            StaticMethods.createResponse(request,response,HttpServletResponse.SC_CREATED, "Created");
            return;
        }

        StaticMethods.createResponse(
                request, response,433,
                "Such Type doesn't exist");
    }


    /**
     * Получение Бренда по его имени и Типу, к которому он принадлежит
     * @param brandName наименование Бренда
     * @param type Тип, к которому принадлежит Бренд
     * @return Надейнный Бренд
     */
    public Brand findByNameAndTypeId(String brandName, Type type) {
        return brandRepository.findByNameAndTypeId(brandName, type);
    }


    /**
     * Удаление Бренда по :id
     * @param body [json] :id Бренда, который необходимо удалить
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 432 - There isn't exist Brand with this id
     */
    public void deleteBrand(String body, HttpServletRequest request, HttpServletResponse response) {
        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        Brand brand = brandRepository.findById(Long.valueOf(id)).orElse(null);
        if(brand!=null) {

            // Поиск всех девайсов для удаления картинок из static/images
            List<Device> list = deviceService.findAllByBrandId(brand);
            for (Device device: list){
                if(device.getIsName())
                    new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + device.getPathFile()).delete();
            }


            brandRepository.delete(brand);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_NO_CONTENT, "No Content");
            return;
        }

        StaticMethods.createResponse(
                request, response, 432, "There isn't exist Brand with this id");
    }


    /**
     * Изменение существующего Бренда по :id
     *
     * @param body [json] :id Бренда и его новое наименование (name)
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 432 - There isn't exist Brand with this id
     */
    public void editBrand(String body, HttpServletRequest request, HttpServletResponse response){
        String id = StaticMethods.parsingJson(body, "id", request, response);
        String name = StaticMethods.parsingJson(body, "name", request, response);
        if(id==null||name==null)
            return;

        Brand brand = brandRepository.findById(Long.valueOf(id)).orElse(null);
        if(brand!=null){
            brand.setName(name);
            brandRepository.save(brand);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_CREATED, "Created");
        }

        StaticMethods.createResponse(
                request, response, 432, "There isn't exist Brand with this id");
    }
}
