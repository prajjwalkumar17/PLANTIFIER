package com.rejointech.planeta.APICalls;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class APICall {
    public static OkHttpClient okhttpmaster() {
        return new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    }

    public static String urlbuilderforhttp(String url) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        return urlBuilder.build().toString();
    }

    public static Request post4signup(String url ,RequestBody requestBody ){
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

    }


}
