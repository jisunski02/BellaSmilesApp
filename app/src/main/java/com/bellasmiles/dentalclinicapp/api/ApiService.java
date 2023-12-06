package com.bellasmiles.dentalclinicapp.api;

import com.bellasmiles.dentalclinicapp.model.AppointmentModel;
import com.bellasmiles.dentalclinicapp.model.ClientProfileModel;
import com.bellasmiles.dentalclinicapp.model.DoctorModel;
import com.bellasmiles.dentalclinicapp.model.LoginRegisterModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;
import com.bellasmiles.dentalclinicapp.model.ServiceModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("servicelist.php")
    Single<List<ServiceModel>> getServices();

    @GET("doctorlist.php")
    Single<List<DoctorModel>> getDoctors();


    @GET("schedulelist.php")
    Single<List<ScheduleModel>> getSchedules();

    @GET("scheduleTimeByDate.php")
    Single<List<ScheduleModel>> getScheduleTimeByDate(@Query("date") String date);


    @GET("viewClientProfile.php")
    Single<List<ClientProfileModel>> getClientProfile(@Query("email_address") String emailAddress);

    @POST("register.php")
    @FormUrlEncoded
    Single<LoginRegisterModel> registerAccount(@Field("firstname") String firstname,
                                               @Field("middlename") String middlename,
                                               @Field("lastname") String lastname,
                                               @Field("gender") String gender,
                                               @Field("contact_no") String contact_no,
                                               @Field("email_address") String email_addrress,
                                               @Field("address") String address,
                                               @Field("birthdate") String birthdate
    );

    @POST("login.php")
    @FormUrlEncoded
    Single<LoginRegisterModel> loginAccount(@Field("username") String username,
                                            @Field("password") String password
    );

    @POST("bookAppointment.php")
    @FormUrlEncoded
    Single<LoginRegisterModel> bookAppointment(@Field("client_id") String client_id,
                                               @Field("doctor_id") String doctor_id,
                                               @Field("service_id") String service_id,
                                               @Field("schedule_id") String schedule_id
    );

    @POST("viewBookingHistory.php")
    @FormUrlEncoded
    Single<List<AppointmentModel>> appointmentHistory(@Field("client_id") String client_id,
                                                @Field("status") String status
    );

}
