package com.khurshid.kamkorapartner.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String APPEND_URL_USER = "/api/users/";
    public static final String APPEND_URL_ORDER = "/api/orders/";
    public static final String APPEND_URL_ADDRESS = "/api/address/";
    public static final String APPEND_URL_PARTNER = "/api/partners/";
    //    public static String Base_URL = "http://192.168.43.61:5000";
//    public static String Base_URL = "http://192.168.43.1:5000";
    public static String Base_URL = "http://3.133.110.209:5000";
    public static String TEST_URL = "https://postman-echo.com";
    static Retrofit retrofit = null;

    public static UserService getInterface() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit.create(UserService.class);

    }

}
