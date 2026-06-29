package com.example.endemikdb.api;

import com.example.endemikdb.model.EndemikResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("endemik.json")
    Call<EndemikResponse> getEndemik();
}