package com.example.crimewave;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommonApi {

    @GET("crime-last-updated")
    Call<lastUpdated>getLastUpdated();
}
