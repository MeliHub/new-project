package com.reidius.lawrenceafriyie.overwatchmap.apiclient;

import com.reidius.lawrenceafriyie.overwatchmap.models.Campus;
import com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService;
import com.reidius.lawrenceafriyie.overwatchmap.models.Login;
import com.reidius.lawrenceafriyie.overwatchmap.models.Person;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;
import com.reidius.lawrenceafriyie.overwatchmap.models.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/EMERGENCYSERVICE")
    Call<List<EmergencyService>> getEmergencyService();

    @POST("api/REPORTINCIDENT")
    Call<ReportIncident> sendReport(@Body ReportIncident reportIncident);

    @POST("api/PERSON")
    Call<Person> person(@Body Person person);

    @POST("api/LOGIN")
    Call<Login> login(@Body Login login);

    @GET("api/LOGIN")
    Call<Login> getLogin(@Query("loginID") Integer loginID, @Query("password") String password);

    @POST("api/STUDENT")
    Call<Student> student(@Body Student student);

    @GET("api/REPORTINCIDENT")
    Call<List<ReportIncident>> getIncidents(@Query("status") String status);
    @GET("api/CAMPUS")
    Call<List<Campus>> getCampus();


}
