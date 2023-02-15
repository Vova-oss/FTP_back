package com.example.demo.Controller.AuxiliaryClasses;

import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;

public class StaticMethods {


//    /**
//     * Создание ответа
//     * @param status - статус ответа
//     * @param info - инфорация, которая будет прописана под полем "info"
//     */
//    public static void createResponse(int status, String info){
//
//        ReqResContext context = ReqResContext.getCurrentInstance();
//        ServerHttpRequest request = context.getRequest();
//        ServerHttpResponse response = context.getResponse();
//
//        response.setStatusCode();
//        response.
//        response.(status);
//        ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON).body()
////        response.addHeader("Access-Control-Allow-Origin", "*" );
//
//        final Map<String, Object> body = new HashMap<>();
//        body.put("status", status);
//        body.put("info", info);
//        body.put("path", request.getServletPath());
//
//        final ObjectMapper mapper = new ObjectMapper();
//        try {
//            mapper.writeValue(response.getOutputStream(), body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * Создание ответа для передачи всех ошибок(данных), которые были обнаружены при валидации
//     * @param field лист полей, к которым относятся ошибки
//     * @param info характеристика каждой ошибки
//     *
//     * @code 400 - Incorrect validation
//     */
//    public static void createBadResponseDueToValidation(List<String> field, List<String> info){
//
//        ReqResContext context = ReqResContext.getCurrentInstance();
//        ServerHttpResponse request = context.getRequest();
//        HttpServletResponse response = context.getResponse();
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(400);
//
//        final Map<String, Object> body = new HashMap<>();
//        body.put("status", 400);
//        body.put("field", field);
//        body.put("info", info);
//        body.put("path", request.getServletPath());
//
//        final ObjectMapper mapper = new ObjectMapper();
//        try {
//            mapper.writeValue(response.getOutputStream(), body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * Получение конкретного поля из json
     * @param body - изначальный json
     * @param field - поле, которое необходимо получить из этого json
     * @return field/null
     *
     * @code 400 - Incorrect JSON
     */
    @SneakyThrows
    public static String parsingJson (String body,
                                      String field) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            field = jsonObject.getString(field);
        } catch (JSONException e) {
            throw new CustomException("Incorrect JSON");
        }
        return field;
    }


    /** метод определения расширения файла */
    public static String getFileExtension(String fileName) {
        // если в имени файла есть точка и она не является первым символом в названии файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".")+1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return null;
    }






    // Задумка прикольная, жаль, что я даун
//    public static <T extends BaseEntity> T findById(String body,
//                                                    HttpServletRequest request ,
//                                                    HttpServletResponse response,
//                                                    JpaRepository<T, Long> repository){
//
//        String field = parsingJson(body, "id");
//        if (field == null)
//            return null;
//
//        List<T> list = repository.findAll();
//        for (T el : list) {
//            if (field.equals(Objects.toString(el.getId()))) {
//                return el;
//            }
//        }
//        return null;
//    }

}
