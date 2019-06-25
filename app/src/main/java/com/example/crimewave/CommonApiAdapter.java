package com.example.crimewave;


import com.example.crimewave.Force.ForcesBasic;
import com.example.crimewave.Force.GetForceInfo;
import com.example.crimewave.Force.SeniorOfficer;
import com.example.crimewave.Force.SpecificForce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommonApiAdapter {
    protected Retrofit retrofitAdapter;
    protected CommonApi mApi;

    public CommonApiAdapter() {
        retrofitAdapter = new Retrofit.Builder()
                .baseUrl(GetForceInfo.FORCEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofitAdapter.create(CommonApi.class);
    }


    public Call<lastUpdated> getLastUpdatedCall(){
        return mApi.getLastUpdated();

    }

}
