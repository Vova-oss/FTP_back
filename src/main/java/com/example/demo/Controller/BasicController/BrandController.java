package com.example.demo.Controller.BasicController;

import com.example.demo.Service.BrandService;
import io.swagger.annotations.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(tags = "Brand")
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @ApiOperation(value = "Добавление нового бренда")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Such Brand of this Type already exist"),
            @ApiResponse(code = 400, message = "Such Type doesn't exist")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Mono<Void> addBrand(
            @ApiParam(value = "Название бренда и название типа",
                      example = "{\n\"name\":\"Apple\",\n\"type\":\"Смартфоны\"\n}",
                      required = true)
            @RequestBody String body) {
        return brandService.addBrand(body).then();
    }


    @ApiOperation(value = "Удаление бренда")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "There isn't exist Brand with this id"),
            @ApiResponse(code = 400, message = "Brand has some connections. Delete the linking objects")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public Mono<Void> deleteBrand(
            @ApiParam(value = ":id бренда, который мы хотим удалить",
                      example = "{\n\"id\":\"5\"\n}",
                      required = true)
            @RequestBody String body){
        return brandService.deleteBrand(body);
    }

    @ApiOperation(value = "Редактирование бренда")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "There isn't exist Brand with this id"),
            @ApiResponse(code = 400, message = "Such Brand of this Type already exist")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/edit")
    public Mono<Void> editBrand(
            @ApiParam(value = ":id бренда, который мы хотим редактировать, новое имя",
                      example = "{\n\"id\":\"5\"\n,\n\"name\":\"Apple\"\n}",
                      required = true)
            @RequestBody String body){
        return brandService.editBrand(body);
    }

}
