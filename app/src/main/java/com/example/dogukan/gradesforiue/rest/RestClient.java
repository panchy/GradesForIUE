package com.example.dogukan.gradesforiue.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dogukan on 01.08.2016.
 */
public class RestClient {
    private static String baseUrl = "https://thor-oasis.izmirekonomi.edu.tr/oasis/";
    private static oasisApi service = null;
    private static CookieJarExtended cookieJar = null;
    public static oasisApi getService()
    {
        if(service==null)
        {
            cookieJar = new CookieJarExtended();
            OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .cookieJar(cookieJar)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

             service = retrofit.create(oasisApi.class);
        }
        return service;
    }



}
