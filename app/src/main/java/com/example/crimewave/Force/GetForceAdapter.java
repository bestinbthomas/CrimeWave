package com.example.crimewave.Force;


import com.example.crimewave.CommonApiAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetForceAdapter {
    protected Retrofit retrofitAdapter;
    protected GetForceInfo mApi;

    public GetForceAdapter() {
        retrofitAdapter = new Retrofit.Builder()
                .baseUrl(CommonApiAdapter.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofitAdapter.create(GetForceInfo.class);
    }


    public Call<ArrayList<ForcesBasic>> getForcesListCall(){
        return mApi.getForcesList();

    }

    public Call<SpecificForce> getSpecificForceCall(String ForceId){
        return mApi.getSpecificForce(ForceId);

    }

}
