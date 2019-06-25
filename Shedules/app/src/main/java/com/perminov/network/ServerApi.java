package com.perminov.network;

import com.perminov.network.pojo.SchedulesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerApi {
    @GET("?apikey=c63e55fb-5205-460c-a52c-1fe9db9dec56")
    Call<SchedulesResponse> getSchedules(@Query("station") String station, @Query("date") String date);//дополнение к BASE_URL
}
