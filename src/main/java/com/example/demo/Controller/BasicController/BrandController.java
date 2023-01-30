package com.example.demo.Controller.BasicController;

import com.example.demo.Service.BrandService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @PostMapping("/add")
    public void addBrand(
            @ApiParam(value = "Название бренда и название типа",
                      example = "{\n\"name\":\"Apple\",\n\"type\":\"Смартфоны\"\n}",
                      required = true)
            @RequestBody String body)  {
        brandService.addBrand(body);
    }


    @ApiOperation(value = "Удаление бренда")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "There isn't exist Brand with this id")
    })
    @DeleteMapping("/delete")
    public void deleteBrand(
            @ApiParam(value = ":id бренда, который мы хотим удалить",
                      example = "{\n\"id\":\"5\"\n}",
                      required = true)
            @RequestBody String body){
        brandService.deleteBrand(body);
    }

    @ApiOperation(value = "Редактирование бренда")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "There isn't exist Brand with this id")
    })
    @PutMapping("/edit")
    public void editBrand(
            @ApiParam(value = ":id бренда, который мы хотим редактировать, новое имя",
                      example = "{\n\"id\":\"5\"\n,\n\"name\":\"Apple\"\n}",
                      required = true)
            @RequestBody String body){
        brandService.editBrand(body);
    }

}
