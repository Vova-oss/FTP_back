package com.example.demo.Controller;

import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(tags = "Sending Images")
@CrossOrigin
@Controller
public class SendingImages {


    @ApiOperation(value = "Отправка картинки по её названию (картинка раскладывается на байты)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 432, message = "Image with this name doesn't exist")
    })
    @GetMapping("/takeImage/{imageName}")
    public ResponseEntity<Resource> sendImage(
            @ApiParam(value = "Имя картинки",
                      example = "4c9fc881-be63-4372-881a-3f4e0962e29cScreenshot_1.png",
                      required = true)
            @PathVariable String imageName,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        Path path =Paths.get(System.getProperty("user.dir").replace("\\","/") + "/src/main/resources/static/images/"+imageName);
        if(!Files.exists(path)){
            StaticMethods.createResponse(
                    request, response, 432,
                    "Image with this name doesn't exist");
            return null;
        }

        //Узнаём расширение картинки
        String imageExtension = StaticMethods.getFileExtension(imageName);

        // Преобразование картинки в массив байтов
        byte[] bytes = Files.readAllBytes(path);
        final ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/" + imageExtension);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .body(byteArrayResource);
    }

}
