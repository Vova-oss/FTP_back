package com.example.demo.Controller.BasicController;

import com.example.demo.DTO.TypeDTO;
import com.example.demo.Entity.Type;
import com.example.demo.Service.TypeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Api(tags = "Type")
@RestController
@RequestMapping("/type")
@CrossOrigin("http://localhost:3000")
public class TypeController {

    @Autowired
    TypeService typeService;


    @ApiOperation(value = "Добавление новогоТипа")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 400, message = "Such Type already exists")
    })
    @PostMapping("/add")
    public Mono<Type> addType(
            @ApiParam(
                    value = "Наименование Типа",
                    example = "{\n\"name\":\"Смартфон\"\n}",
                    required = true
            )
            @RequestBody String typeName)  {
        return typeService.addType(typeName);
    }

    @ApiOperation(value = "Получение всех Типов и принадлежащих к ним Брендов")
    @GetMapping("/getAll")
    public Flux<TypeDTO> getAll(){
        return typeService.getAllAsDTO();
    }


//    @ApiOperation(value = "Удаление Типа по :id")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "---"),
//            @ApiResponse(code = 204, message = "No Content"),
//            @ApiResponse(code = 400, message = "There isn't exist Type with this :id")
//    })
//    @DeleteMapping("/delete")
//    public void deleteType(
//            @ApiParam(
//                    value = ":id Типа, который необходимо удалить",
//                    example = "{\n\"id\":\"5\"\n}",
//                    required = true
//            )
//            @RequestBody String body){
//        typeService.deleteType(body);
//    }
//
//
//    @ApiOperation(value = "Изменение Типа")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "---"),
//            @ApiResponse(code = 201, message = "Created"),
//            @ApiResponse(code = 400, message = "There isn't exist Type with this :id")
//    })
//    @PutMapping("/edit")
//    public void editType(
//            @ApiParam(
//                    value = ":id Типа и его новое имя",
//                    example = "{\n\"id\":\"5\",\n\"name\":\"Холодильник\"\n}",
//                    required = true
//            )
//            @RequestBody String body){
//        typeService.editType(body);
//    }


}
