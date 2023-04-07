package com.example.checkinemployee;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("add")
    Call<DataModal> createPost(@Body DataModal dataModal);
}


