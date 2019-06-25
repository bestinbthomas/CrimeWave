package com.example.crimewave.Force;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetForceInfo {

    String FORCEURL = "https://data.police.uk/api/";

    @GET("forces")
    Call<ArrayList<ForcesBasic>> getForcesList();

    @GET("forces/{forceid}")
    Call<SpecificForce> getSpecificForce(@Path("forceid") String forceid);

    @GET("force/{forceid}/people")
    Call<ArrayList<SeniorOfficer>> getSeniorOfficers(@Path("forceid") String forceid);
}
