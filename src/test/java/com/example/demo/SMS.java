package com.example.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SMS {

    @Test
    public void createResponse(){



        Request request = new Request.Builder().method("GET", null)
                .url("https://sms.ru/sms/send?api_id=6CCCD96B-FD2A-CFF3-8E70-6A1B7296366E&to=79645932177&msg=how+are+you?&json=1")
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try
        {
            Response response = httpClient.newBuilder()
                    .readTimeout(1, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .execute();

            if(response.isSuccessful()) {
                System.out.println("Successful");
            }
            else {
                System.out.println("Very bad");
            }
        }
        catch (IOException ignored){}
    }

}
