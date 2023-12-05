package com.bellasmiles.dentalclinicapp.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientRx {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();


            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);

            httpClient.protocols(Util.immutableList(Protocol.HTTP_1_1));

            OkHttpClient client = httpClient.build();
            client.dispatcher().cancelAll();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        else {
            if (!retrofit.baseUrl().toString().equals(baseUrl)) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS);
                httpClient.addInterceptor(logging);

                OkHttpClient client = httpClient.build();
                client.dispatcher().cancelAll();


                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }


        }
        return retrofit;
    }

}
