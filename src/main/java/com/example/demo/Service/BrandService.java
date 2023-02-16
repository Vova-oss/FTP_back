package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.TypeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BrandService {

    @Autowired
    BrandRepository brandRepository;
    @Autowired
    TypeRepository typeRepository;

//    @Autowired
//    TypeService typeService;
//    @Autowired
//    DeviceService deviceService;
//

    public Flux<Brand> getAllByTypeId(Long typeId){
        return brandRepository.findAllByTypeId(typeId);
    }



    /**
     * Добавление нового Бренда
     * @param body [json] название Типа(type),
     *             которому принадлежит Бренд, и название нового Бренда(name)
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - Such Brand of this Type already exist
     * @code 400 - Such Type doesn't exist
     */
    public Mono<Void> addBrand(String body) {
        return typeRepository
                .findByName(StaticMethods.parsingJson(body, "type"))
                .switchIfEmpty(Mono.error(new CustomException("Such Type doesn't exist")))
                .flatMap(type -> {
                    Brand brand = new Brand();
                    try {
                        JSONObject obj = new JSONObject(body);
                        String name = obj.getString("name");
                        brand.setName(name);
                        brand.setTypeId(type.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return brandRepository
                            .save(brand)
                            .onErrorResume(throwable -> Mono.error(new CustomException("Such Brand of this Type already exist")));
                })
                .then(Mono.empty());
    }


//    /**
//     * Получение Бренда по его имени и Типу, к которому он принадлежит
//     * @param brandName наименование Бренда
//     * @param type Тип, к которому принадлежит Бренд
//     * @return Надейнный Бренд
//     */
//    public Brand findByNameAndTypeId(String brandName, Type type) {
//        return brandRepository.findByNameAndTypeId(brandName, type);
//    }


    /**
     * Удаление Бренда по :id
     * @param body [json] :id Бренда, который необходимо удалить
     * @code 204 - No Content
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Brand with this id
     * @code 400 - Brand has some connections. Delete the linking objects
     */
    public Mono<Void> deleteBrand(String body) {
        Long id = Long.valueOf(StaticMethods.parsingJson(body, "id"));

        return brandRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new CustomException("There isn't exist Brand with this id")))
                .flatMap(brand ->
                    brandRepository
                            .delete(brand)
                            .onErrorResume(throwable -> Mono.error(new CustomException("Brand has some connections. Delete the linking objects")))
                );
    }


    /**
     * Изменение существующего Бренда по :id
     *
     * @param body [json] :id Бренда и его новое наименование (name)
     *
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - There isn't exist Brand with this id
     * @code 400 - Such Brand of this Type already exist
     */
    public Mono<Void> editBrand(String body){
        Long id = Long.valueOf(StaticMethods.parsingJson(body, "id"));
        String name = StaticMethods.parsingJson(body, "name");

        return brandRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new CustomException("There isn't exist Brand with this id")))
                .flatMap(brand -> {
                    brand.setName(name);
                    return brandRepository
                            .save(brand)
                            .onErrorResume(throwable -> Mono.error(new CustomException("Such Brand of this Type already exist")));
                })
                .then(Mono.empty());
    }
}
