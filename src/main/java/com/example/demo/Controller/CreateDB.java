package com.example.demo.Controller;

import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Device_info;
import com.example.demo.Entity.Response.ResponseClass;
import com.example.demo.Entity.Type;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.DeviceRepository;
import com.example.demo.Repositories.Device_infoRepository;
import com.example.demo.Repositories.TypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Api(tags = "Create DB")
@CrossOrigin
@RestController
public class CreateDB {

    @Autowired
    TypeRepository typeRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    Device_infoRepository device_infoRepository;

    int amountOfBrands = 0;


    @Async
    @ApiOperation(value = "Парсинг данных (Метод выполняется ассинхронно, " +
            "все данные выводятся в консоль на сервере)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---")
    })
    @GetMapping("/createDB")
    public void createDB() throws IOException {
        Document document = selectDocumentByHref("https://www.holodilnik.ru/");
        Elements types = document.getElementsByClass( "menu-categories__item");
        for(int i = 0; i < 8;i++){
            if(i!=2 && i!=4 && i!=6){
                Element element = types.get(i);
                workWithSubtypes(element.child(0).text(), element.child(0).attr("data-href"));
            }

        }

        System.out.println("DB was created correctly");
    }


    public void workWithSubtypes(String nameOfType, String href)  {

        try{
            typeRepository.save(createType(nameOfType));
        }
        catch (DataIntegrityViolationException e){

        }

        Document document = selectDocumentByHref("https:" + href);

        Elements allSubtypes = document.getElementsByClass("categories__item-list__title");
//        AtomicBoolean flag = new AtomicBoolean(false);
        allSubtypes.forEach(element -> {
            if(element.child(0).text().equals(nameOfType)){
                searchingBrands(nameOfType, element.child(0).attr("href"));
//                flag.set(true);
            }
        });


//        if(!flag.get()){
//            Element element = allSubtypes.get(2);
//            searchingBrands(nameOfType, element.child(0).attr("href"));
//        }

    }

    public void searchingBrands(String nameOfType, String href){
        Document document = selectDocumentByHref("https:" + href);

        Element fieldWithBrands = document.getElementById("div_filter_vendor");
        Elements allBrands = fieldWithBrands.
                getElementsByClass("field-checkbox__label form-check-label beforeSelect");
        for(int i = 0; i < allBrands.size(); i++){
            Element element = allBrands.get(i);
            brandRepository.save(createBrand(nameOfType, element.text().substring(0, element.text().indexOf(" "))));
            inOneBrand(
                    nameOfType,
                    element.text().substring(0, element.text().indexOf(" ")),
                    element.child(1).attr("href"));
        }
    }


    public void inOneBrand(String nameOfType, String brandName, String href){

        Document document = selectDocumentByHref("https:" + href);
        Elements fields = document.getElementsByClass("goods-tile preview-product");

        // Наименования девайсов
        Elements names = fields.select("div[class=product-name]");
        List<String> nameOfDevicesList = new ArrayList<>();
        names.forEach(element -> nameOfDevicesList.add(element.text()));

        // Пути до картинок девайсов
        Elements pictures = fields.select("div[class=col product-image]");
        Elements imgs = pictures.select("img");
        List<String> pathOfPicturesList = new ArrayList<>();
        imgs.forEach(img -> pathOfPicturesList.add("https:" + img.attr("src")));

        // Дополнительная информация о девайсах
        Elements tables = fields.select("table[class=table table-borderless]");
        Map<Integer, List<List<String>>> map = new HashMap<>();
        for(Element table: tables){
            Elements trs = table.select("tr");

            List<List<String>> allInfo= new ArrayList<>();
            for(Element tr: trs){
                List<String> oneLine = new ArrayList<>();
                if(tr.childrenSize()==2) {
                    oneLine.add(tr.child(0).text());
                    oneLine.add(tr.child(1).text());
                    allInfo.add(oneLine);
                }
            }
            map.put(tables.indexOf(table), allInfo);
        }

        // Цена девайсов
        Elements prices = fields.select("div[class=price]");
        List<String> priceOfDevicesList = new ArrayList<>();
        for(Element price: prices){
            String temp = price.text().replaceAll("[ руб.]","");
            if(temp.length()>6){
                temp = temp.substring(0,4);
            }
            priceOfDevicesList.add(temp);
        }


        // Сохранение всех девайсов
        for(int i = 0; i < nameOfDevicesList.size(); i++){
            try {
                Device device = createDevice(
                        nameOfType,
                        brandName,
                        nameOfDevicesList.get(i),
                        priceOfDevicesList.get(i),
                        pathOfPicturesList.get(i));
                if(device.getPathFile().length()>6) {
                    deviceRepository.save(device);

                    List<List<String>> list = map.get(i);
                    for (List<String> characters : list) {
                        Device_info device_info = createDevice_info(device, characters.get(0), characters.get(1));
                        device_infoRepository.save(device_info);
                    }
                }
            }catch (IndexOutOfBoundsException e ){
                System.out.println("Какой-то из листов вышел за рамки");
                break;
            }catch(DataIntegrityViolationException ee){
                System.out.println("Повторяющееся имя девайса:" + nameOfDevicesList.get(i));
            }


        }

        try {
            System.out.println(++amountOfBrands);
            Thread.sleep(10_000); // Задержка после добавления целого бренда в БД (1сек=1000millis)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Переход на следующую страницу
        Element buttonNext = document.select("li[class=page-item page-next]").first();
        if(buttonNext!=null)
            inOneBrand(nameOfType, brandName, buttonNext.child(0).attr("href"));

    }

    public Type createType(String name){
        Type type = new Type();
        type.setName(name);
        return type;
    }

    public Brand createBrand(String nameOfType, String nameOfBrand){
        Brand brand = new Brand();
        brand.setName(nameOfBrand);
        brand.setTypeId(typeRepository.findByName(nameOfType));
        return brand;
    }

    public Device createDevice(String nameOfType,
                               String nameOfBrand,
                               String nameOfDevice,
                               String price,
                               String pathOfPicture){

        Type type = typeRepository.findByName(nameOfType);
        Brand brand = brandRepository.findByNameAndTypeId(nameOfBrand, type);

        Device device = new Device();

        device.setTypeId(type);
        device.setBrandId(brand);
        device.setName(nameOfDevice);
        device.setPrice(price);

        device.setPathFile(pathOfPicture);
        device.setIsName(false);
        device.setDataOfCreate(System.currentTimeMillis());
        return device;
    }

    public Device_info createDevice_info(Device device, String title, String description){
        Device_info device_info = new Device_info();
        device_info.setDevice(device);
        device_info.setTitle(title);
        device_info.setDescription(description);
        return device_info;
    }

    // Вытаскиваем страницу по href через случайные host и port
    public Document selectDocumentByHref(String href) {

        Document document = null;
        try {
            document = Jsoup.parse(new URL(href), 3000);
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }





    String namesOfType = "Смартфоны";
    String firstBrand = "Apple";
    List<String> namesOfDevicesOfApple = Arrays.asList("IPhone 12 (белый)", "IPhone 12 (чёрный)", "IPhone 12 Pro Max",
            "IPhone 12 Pro","IPhone 11 (красный)","IPhone 11 (белый)", "IPhone 11 (черный)", "IPhone 11 (жёлтый)",
            "IPhone 12 mini (фиолетовый)","IPhone 12 mini (синий)",
            "IPhone 12 (красный)", "IPhone 12 (зелёный)");
    List<String> pathOfPictureOfApple = Arrays.asList("https://pngimg.com/uploads/iphone_12/small/iphone_12_PNG36.png",
            "https://pngimg.com/uploads/iphone_12/small/iphone_12_PNG37.png", "https://www.cifrus.ru/photos/medium/apple/apple-iphone-12-pro-128gb-grey-a2341-ll-1.jpg",
            "https://www.cifrus.ru/photos/little/apple/apple-iphone-12-pro-128gb-grey-a2341-ll-3.jpg", "https://www.cifrus.ru/photos/little/apple/apple-iphone-11-64gb-red-a2111-3.jpg",
            "https://www.cifrus.ru/photos/little/apple/apple-iphone-11-64gb-white-a2111-3.jpg", "https://www.cifrus.ru/photos/little/apple/apple-iphone-11-64gb-black-a2111-3.jpg",
            "https://www.cifrus.ru/photos/little/apple/apple-iphone-11-64gb-yellow-a2111-3.jpg", "https://www.cifrus.ru/photos/little/apple/apple-iphone-12-mini-64gb-purple-a2176-ll-3.jpg",
            "https://www.cifrus.ru/photos/little/apple/apple-iphone-12-mini-64gb-blue-a2172-ll-3.jpg", "https://pngimg.com/uploads/iphone_12/small/iphone_12_PNG8.png",
            "https://pngimg.com/uploads/iphone_12/small/iphone_12_PNG17.png");


    @Async
    @ApiOperation(value = "Парсинг данных (12 Девайсов для Apple, Смартфоны)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---")
    })
    @GetMapping("/createMiniDB")
    public void createMiniDB(){

        try {
            typeRepository.save(createType(namesOfType));
        } catch (Exception ignored) {}

        try {
            brandRepository.save(createBrand(namesOfType, firstBrand));
        } catch (Exception ignored) {}

        for(String device: namesOfDevicesOfApple){
            try {
                deviceRepository.save(createDevice(namesOfType, firstBrand, device, String.valueOf((int) (Math.random() * 10000)),
                        pathOfPictureOfApple.get(namesOfDevicesOfApple.indexOf(device))));
            } catch (Exception ignored) {}
        }

        System.out.println("All right");
    }

}
