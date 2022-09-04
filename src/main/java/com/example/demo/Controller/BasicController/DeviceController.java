package com.example.demo.Controller.BasicController;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Response.DeviceWIthNecessaryParameters;
import com.example.demo.Service.DeviceService;
import io.swagger.annotations.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "Device")
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @ApiOperation(value = "Добавление девайсов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 432, message = "This name of Device already exists"),
            @ApiResponse(code = 433, message = "This Type doesn't exist"),
            @ApiResponse(code = 434, message = "This Brand of this Type doesn't exist"),
            @ApiResponse(code = 435, message = "Incorrect image extension")
    })
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void addDevice(
            @ApiParam(
                    name = "Device",
                    value = "Все данные о девайсе:\nbrand - Бренд, к которому будет принадлежать Девайс" +
                            "\ntype - Тип, к которому будет принадлежать Девайс\nname - название Девайса" +
                            "\nprice - цена Девайса\nОдно из двух(imgFile или imgRef):\nimgFile - файл-фото\n" +
                            "imgRef - путь до изображения из инета\n" +
                            "characteristic - характеристики Девайса\ntitle - титл\ndescription - описание",
                    example = "{{\nbrand: \"\",\ntype: \"\",\nname: \"Iphone 12 pro\", \n" +
                            "price: \"25000\" , \n    imgFile: *Файл*\n        /\n" +
                            "    imgRef: *путь до изображения*\ncharacteristic: [\n" +
                            "     {\n       title: \"\"\n       description: \"\"\n" +
                            "     },\n     {...}\n]\n}}",
                    required = true
            )
            @RequestParam(value = "imgFile", required = false) MultipartFile file,
            @ApiParam(hidden = true)
            @RequestParam("brand") String brand,
            @ApiParam(hidden = true)
            @RequestParam("type") String type,
            @ApiParam(hidden = true)
            @RequestParam(value = "imgRef", required = false) String ref,
            @ApiParam(hidden = true)
            @RequestParam("name") String name,
            @ApiParam(hidden = true)
            @RequestParam("price") String price,
            @ApiParam(hidden = true)
            @RequestParam("characteristic") JSONArray list,
            HttpServletRequest request,
            HttpServletResponse response){

        deviceService.addDevice(brand, type, file, ref, name, price, list, request, response);

    }


    @ApiOperation(value = "Получение Девайсов по параметрам")
    @ApiResponses(value = {
            @ApiResponse(code = 432, message = "The minPrice more than the maxPrice"),
            @ApiResponse(code = 433, message = "Devices with this type doesn't exists"),
            @ApiResponse(code = 434, message = "Devices with this brands doesn't exists"),
            @ApiResponse(code = 435, message = "Devices with this price doesn't exists"),
            @ApiResponse(code = 436, message = "Incorrect data of page or limit")
    })
    @GetMapping("/getByParams")
    public DeviceWIthNecessaryParameters getByParams(
            @ApiParam(
                    value = "Наименование Типа",
                    example = "Смартфоны",
                    required = true
            )
             @RequestParam("type") String type,
            @ApiParam(
                    value = "Наименование Брендов",
                    example = "Apple"
            )
             @RequestParam(value = "brand", required = false) List<String> brand,
            @ApiParam(
                    value = "Номер страницы",
                    example = "5"
            )
             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @ApiParam(
                    value = "Количество девайсов, которые отображаются на одной странице",
                    example = "50"
            )
             @RequestParam(value = "limit", required = false , defaultValue = "20") int limit,
            @ApiParam(
                    value = "Минимальная цена девайса",
                    example = "1500"
            )
             @RequestParam(value = "minPrice", required = false, defaultValue = "0") int minPrice,
            @ApiParam(
                    value = "Максимальная цена девайса",
                    example = "5000"
            )
             @RequestParam(value = "maxPrice", required = false, defaultValue = "-1") int maxPrice,
                            HttpServletRequest request,
                            HttpServletResponse response){

        return deviceService.getByParams(type, brand, page, limit, minPrice, maxPrice, request, response);
    }


    @ApiOperation(value = "Удаление девайса")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 432, message = "There isn't exist Device with this id")
    })
    @DeleteMapping("/delete")
    public void deleteDevice(
            @ApiParam(
                    value = ":id Девайса, который необходимо удалить",
                    example = "{\n\"id\":\"5\"\n}"
            )
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response){
        deviceService.deleteDevice(body, request, response);
    }


    @ApiOperation(value = "Изменение девайсов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Incorrect JSON"),
            @ApiResponse(code = 432, message = "This name of Device already exists"),
            @ApiResponse(code = 433, message = "This Type doesn't exist"),
            @ApiResponse(code = 434, message = "This Brand of this Type doesn't exist"),
            @ApiResponse(code = 435, message = "Incorrect image extension")
    })
    @PutMapping(value = "/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void editDevice(

            @ApiParam(
                    name = "Device",
                    value = "Все данные о девайсе:\nid - :id существующего Девайса, который желаем изменить" +
                            "\nbrand - Бренд, к которому будет принадлежать Девайс" +
                            "\ntype - Тип, к которому будет принадлежать Девайс\nname - название Девайса" +
                            "\nprice - цена Девайса\nОдно из двух(imgFile или imgRef):\nimgFile - файл-фото\n" +
                            "imgRef - путь до изображения из инета\n" +
                            "characteristic - характеристики Девайса\nid - :id характеристики" +
                            "\ntitle - титл\ndescription - описание",
                    example = "{{\nid:\"\"\nbrand: \"\",\ntype: \"\",\nname: \"Iphone 12 pro\", \n" +
                            "price: \"25000\" , \n    imgFile: *Файл*\n        /\n" +
                            "    imgRef: *путь до изображения*\ncharacteristic: [\n" +
                            "     {\n       id: \"\"\n       title: \"\"\n       description: \"\"\n" +
                            "     },\n     {...}\n]\n}}",
                    required = true
            )
            @RequestParam(value = "imgFile", required = false) MultipartFile file,
            @ApiParam(hidden = true)
            @RequestParam("id") String id,
            @ApiParam(hidden = true)
            @RequestParam("brand") String brand,
            @ApiParam(hidden = true)
            @RequestParam("type") String type,
            @ApiParam(hidden = true)
            @RequestParam(value = "imgRef", required = false) String ref,
            @ApiParam(hidden = true)
            @RequestParam("name") String name,
            @ApiParam(hidden = true)
            @RequestParam("price") String price,
            @ApiParam(hidden = true)
            @RequestParam("characteristic") JSONArray list,
            HttpServletRequest request,
            HttpServletResponse response) {
        deviceService.editDevice(id, brand, type, ref,file, name, price, list, request, response);
    }


    @ApiOperation(value = "Получение девайса по :id")
    @ApiResponses(value = {
            @ApiResponse(code = 432, message = "There isn't exist Device with this id")
    })
    @GetMapping("/getById/{id}")
    public DeviceDTO getById(
            @ApiParam(
                    value = ":id Девайса, который необходимо получить",
                    example = "5",
                    required = true
            )
            @PathVariable String id,
            HttpServletRequest request,
            HttpServletResponse response){
        return deviceService.getDTOById(id, request, response);
    }



    // For my tests
    @ApiOperation(value = "Получение всех девайсов без обработки от лишних полей", hidden = true)
    @GetMapping("/getAll")
    public List<Device> getAll(){
        return deviceService.getAll();
    }


}
