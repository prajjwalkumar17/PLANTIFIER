package com.rejointech.planeta.APICalls;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.rejointech.planeta.Utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class APICall {
    public static OkHttpClient okhttpmaster() {
        return new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)).build();
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

    public static Request del4deletenote(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .delete(requestBody)
                .build();
    }

    public static Request post4createnotes(String url, String usertoken, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", Constants.bearer + usertoken)
                .post(requestBody)
                .build();
    }

    public static Request patch4updatenotes(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .patch(requestBody)
                .build();
    }

    public static Request patch4updateprofile(String url, String usertoken, RequestBody requestBody) {
        return new Request.Builder()
                .patch(requestBody)
                .header("Authorization", Constants.bearer + usertoken)
                .url(url)
                .build();
    }

    public static Request get4profiledata(String url, String userauthtoken) {
        return new Request.Builder()
                .header("Authorization", Constants.bearer + userauthtoken)
                .url(url)
                .build();

    }

    public static Request get4historydata(String url, String userauthtoken) {
        return new Request.Builder()
                .header("Authorization", Constants.bearer + userauthtoken)
                .url(url)
                .build();

    }

    public static Request get4leaderboard(String url) {
        return new Request.Builder()
                .url(url)
                .build();

    }

    public static Request get4allnotes(String url, String userauthtoken) {
        return new Request.Builder()
                .header("Authorization", Constants.bearer + userauthtoken)
                .url(url)
                .build();
    }

    public static Request get4alldashboarditems(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }


    public static Request post4imageupload(String url, String userauthtoken, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", Constants.bearer + userauthtoken)
                .post(requestBody)
                .build();
    }

    public static Request post4secondtimeapicall(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    public static RequestBody buildrequest4updatingprofile(String name, String email) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("email", email)
                .build();
    }

    public static RequestBody buildrequest4secondtimeapicall(String postid) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("postid", postid)
                .build();
    }

    public static RequestBody buildrequest4updatingnote(String noteid, String note) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("note", note)
                .addFormDataPart("noteid", noteid)
                .build();
    }

    public static RequestBody buildrequest4deletenote(String noteid) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("noteid", noteid)
                .build();
    }

    public static RequestBody buildrequest4createnote(String postid, String note) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("note", note)
                .addFormDataPart("postid", postid)
                .build();
    }


    public static RequestBody buildrequstbody4imageupload(String encodedstring) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("base64", encodedstring)
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



}
