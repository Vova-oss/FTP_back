package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.DTO.BrandDTO;
import com.example.demo.DTO.TypeDTO;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.TypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class TypeService {


    @Autowired
    TypeRepository typeRepository;
    @Autowired
    BrandRepository brandRepository;

//    @Autowired
//    BrandService brandService;

//    @Autowired
//    DeviceService deviceService;


    /**
     * Добавление Типа
     * @param name название Типа
     * @code 201 - Created
     * @code 400 - Such Type already exists
     */
    @SneakyThrows
    public Mono<Type> addType(String name){
        Type type;
        try {
            type = new ObjectMapper().readValue(name, Type.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException("Incorrect JSON");
        }

        return typeRepository
                .save(type)
                .onErrorResume(throwable -> Mono.error(new CustomException("Such Type already exists")));

    }


    /** Получение абсолютно всех Типов */
    public Flux<Type> getAll() {
        return typeRepository.findAll();
    }

    public Flux<TypeDTO> getAllAsDTO() {
        return getAll()
                .publishOn(Schedulers.boundedElastic())
                .map(type -> {
                    TypeDTO typeDTO = new TypeDTO();
                    typeDTO.setId(type.getId());
                    typeDTO.setName(type.getName());

                    brandRepository
                            .findAllByTypeId(type.getId())
                            .map(BrandDTO::create)
                            .collectList()
                            .map(brandDTOS -> {typeDTO.setBrands(brandDTOS);return brandDTOS;}).block();

                    return typeDTO;
                });
    }

    public Flux<TypeDTO> findTypeById(Long id) {

        Mono<List<BrandDTO>> brandDTOMono =  brandRepository
                .findAllByTypeId(id)
                .map(BrandDTO::create)
                .collectList();

        Mono<TypeDTO> typeMono = typeRepository
                .findById(id)
                .map(type -> {
                    TypeDTO typeDTO = new TypeDTO();
                    typeDTO.setId(type.getId());
                    typeDTO.setName(type.getName());
                    return typeDTO;
                });

        return typeMono
                .zipWith(brandDTOMono)
                .map(tuple2 ->{
                    TypeDTO type = tuple2.getT1();
                    List<BrandDTO> brandDTOS = tuple2.getT2();
                    type.setBrands(brandDTOS);
                    return type;
        }).flux();

    }


    /**
     * Получение Типа по его названию
     * @param typeName название Типа
     */
    public Mono<Type> findByName(String typeName) {
        return typeRepository.findByName(typeName);
    }


//    /**
//     * Удаление Типа по его :id
//     * @param body [json] содержит :id Типа
//     * @code 204 - No Content
//     * @code 400 - Incorrect JSON
//     * @code 400 - There isn't exist Type with this :id
//     */
//    public void deleteType(String body) {
//
//        String id = StaticMethods.parsingJson(body, "id");
//        if(id == null)
//            return;
//        Type type = typeRepository.findById(Long.valueOf(id)).orElse(null);
//
//        if(type!=null) {
//
//            // Поиск всех девайсов для удаления картинок из static/images
//            List<Device> list = deviceService.findAllByTypeId(type);
//            for(Device device: list){
//                if(device.getIsName())
//                    new File(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/" + device.getPathFile()).delete();
//            }
//
//            typeRepository.delete(type);
//            StaticMethods.createResponse(HttpServletResponse.SC_NO_CONTENT, "No Content");
//        }
//
//        StaticMethods.createResponse( 400, "There isn't exist Type with this :id");
//    }
//
//
//    /**
//     * Изменение Типа по его :id
//     * @param body [json] содержит:
//     *             id - :id Типа, который желаем изменить
//     *             name - новое название Типа
//     * @code 201 - Created
//     * @code 400 - Incorrect JSON
//     * @code 400 There isn't exist Type with this :id
//     */
//    public void editType(String body) {
//
//        String id = StaticMethods.parsingJson(body, "id");
//        if(id == null)
//            return;
//        Type type = typeRepository.findById(Long.valueOf(id)).orElse(null);
//
//        if(type != null){
//            String name = StaticMethods.parsingJson(body,"name");
//            type.setName(name);
//            typeRepository.save(type);
//            StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "Created");
//        }
//
//        StaticMethods.createResponse( 400, "There isn't exist Type with this :id");
//    }



}
