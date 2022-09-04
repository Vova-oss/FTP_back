package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.TypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Service
public class TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    DeviceService deviceService;


    /**
     * Добавление Типа
     * @param name название Типа
     * @code 201 - Created
     * @code 432 - Such Type already exists
     */
    public void addType(String name, HttpServletRequest request, HttpServletResponse response){
        Type type;
        try {
            type = new ObjectMapper().readValue(name, Type.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            StaticMethods.createResponse(
                    request, response, 400, "Incorrect JSON");
            return;
        }

        if(!typeRepository.existsByName(type.getName())){
            typeRepository.save(type);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_CREATED, "Created");
            return;
        }
        StaticMethods.createResponse(
                request, response, 432, "Such Type already exists");
    }


    /** Получение абсолютно всех Типов */
    public List<Type> getAll() {
        return typeRepository.findAll();
    }

    /**
     * Получение Типа по его названию
     * @param typeName название Типа
     */
    public Type findByName(String typeName) {
        return typeRepository.findByName(typeName);
    }


    /**
     * Удаление Типа по его :id
     * @param body [json] содержит :id Типа
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 432 - There isn't exist Type with this :id
     */
    public void deleteType(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        Type type = typeRepository.findById(Long.valueOf(id)).orElse(null);

        if(type!=null) {

            // Поиск всех девайсов для удаления картинок из static/images
            List<Device> list = deviceService.findAllByTypeId(type);
            for(Device device: list){
                if(device.getIsName())
                    new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + device.getPathFile()).delete();
            }

            typeRepository.delete(type);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_NO_CONTENT, "No Content");
        }

        StaticMethods.createResponse(
                request, response, 432, "There isn't exist Type with this :id");
    }


    /**
     * Изменение Типа по его :id
     * @param body [json] содержит:
     *             id - :id Типа, который желаем изменить
     *             name - новое название Типа
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 432 There isn't exist Type with this :id
     */
    public void editType(String body, HttpServletRequest request, HttpServletResponse response) {

        String id = StaticMethods.parsingJson(body, "id", request, response);
        if(id == null)
            return;
        Type type = typeRepository.findById(Long.valueOf(id)).orElse(null);

        if(type != null){
            String name = StaticMethods.parsingJson(body,"name", request, response);
            type.setName(name);
            typeRepository.save(type);
            StaticMethods.createResponse(request, response, HttpServletResponse.SC_CREATED, "Created");
        }

        StaticMethods.createResponse(
                request, response, 432, "There isn't exist Type with this :id");
    }





}
