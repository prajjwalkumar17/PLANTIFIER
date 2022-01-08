package com.rejointech.planeta.APICalls;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
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

    public static Request post4signup(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    public static Request post4ootpverificfation(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    public static RequestBody buildrequestbody4signup(String Name,
                                                      String Email,
                                                      String Phone,
                                                      String PasswordConfirmed,
                                                      String Password) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", Name)
                .addFormDataPart("email", Email)
                .addFormDataPart("phone", Phone)
                .addFormDataPart("password", Password)
                .addFormDataPart("passwordConfirm", PasswordConfirmed)
                .addFormDataPart("role", "test")
                .build();
    }

    public static RequestBody buildrequestbody4signin(String Email,
                                                      String Password) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", Email)
                .addFormDataPart("password", Password)
                .build();
    }

    public static RequestBody buildreq4otpverification(String email,
                                                       String otp) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("otp", otp)
                .addFormDataPart("email", email)
                .build();

    }


}
