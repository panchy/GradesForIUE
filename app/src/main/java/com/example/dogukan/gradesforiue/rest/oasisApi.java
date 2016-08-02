package com.example.dogukan.gradesforiue.rest;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by dogukan on 01.08.2016.
 */
public interface oasisApi {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginFirstPart(@Field("userid") String userid,@Field("password") String pass,@Field("Submit") String submit);

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginSecondPart(@Field("ssno") String pin,@Field("ltype") String ltype,@Field("Submit") String submit);

    @GET("logout.php")
    Call<ResponseBody> logout();

    @GET("student/courses/my_grades/my_grades_view.php?p=Student_Courses")
    Call<ResponseBody> getGrades();

    @FormUrlEncoded
    @POST("student/courses/my_grades/my_grades_view.php?p=Student_Courses")
    Call<ResponseBody> getGradesSearch(@Field("year") String year,@Field("semester1") String semester,@Field("formposted") String formposted);

    @GET("student/help/index.php")
    Call<ResponseBody> getHelp();

    @GET("student/profile/index.php")
    Call<ResponseBody> getProfile();

}
