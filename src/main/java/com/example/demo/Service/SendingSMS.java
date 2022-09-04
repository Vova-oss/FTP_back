package com.example.demo.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class SendingSMS {

    @Value("${api_id}")
    private String apiId;

    /**
     * Отправка СМС на номер телефона. В СМС будет содержаться код, который необходим для
     * подтверждения того, что пользователь является владельцем данного номера
     * @param telephoneNumber телефонный номер
     * @param code код
     */
    public void createSMS(String telephoneNumber, String code){

        Request request = new Request.Builder().method("GET", null)
                .url("https://sms.ru/sms/send?api_id="+apiId+"&to=" +
                        telephoneNumber + "&msg="+code+"&json=1")
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try
        {
            Response response = httpClient.newBuilder()
                    .readTimeout(1, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .execute();


            if(!response.isSuccessful()) {
                System.out.println(response);
            }
            response.close();
        }
        catch (IOException ignored){}
    }

}
