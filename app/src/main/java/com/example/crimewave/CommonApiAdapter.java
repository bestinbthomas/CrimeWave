package com.example.crimewave;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommonApiAdapter {
    protected Retrofit retrofitAdapter;
    protected CommonApi mApi;
    public static final String BASEURL = "https://data.police.uk/api/";

    public CommonApiAdapter() {
        retrofitAdapter = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofitAdapter.create(CommonApi.class);
    }


    public Call<lastUpdated> getLastUpdatedCall(){
        return mApi.getLastUpdated();

    }

}
