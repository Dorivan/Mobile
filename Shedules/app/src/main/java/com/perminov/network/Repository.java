package com.perminov.network;

import android.util.Log;

import com.perminov.network.pojo.SchedulesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository mInstance;

    public static Repository getInstance() {
        if (mInstance == null) {
            mInstance = new Repository();
        }
        return mInstance;
    }

    public void getSchedules(final ResponseCallback<SchedulesResponse> responseCallback, String station, String date) {
        NetworkService.getInstance().getJSONApi().getSchedules(station, date).enqueue(new Callback<SchedulesResponse>() {
            @Override
            public void onResponse(Call<SchedulesResponse> call, Response<SchedulesResponse> response) {
                if (response.isSuccessful()) {
                    SchedulesResponse schedulesResponse = response.body();

                    if (schedulesResponse != null) {
                        responseCallback.onEnd(schedulesResponse);
                    }

                }
            }

            @Override
            public void onFailure(Call<SchedulesResponse> call, Throwable t) {

            }
        });
    }
}
