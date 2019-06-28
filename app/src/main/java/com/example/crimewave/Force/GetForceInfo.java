package com.example.crimewave.Force;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetForceInfo {



    @GET("forces")
    Call<ArrayList<ForcesBasic>> getForcesList();

    @GET("forces/{forceid}")
    Call<SpecificForce> getSpecificForce(@Path("forceid") String forceid);

}
