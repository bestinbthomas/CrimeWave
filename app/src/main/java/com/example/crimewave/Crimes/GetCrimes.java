package com.example.crimewave.Crimes;

import com.example.crimewave.CommonApiAdapter;
import com.example.crimewave.Neighbourhood;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class GetCrimes {

    Retrofit retrofit;
    API mapi;

    interface API {
        @GET("crimes-at-location")
        Call<ArrayList<Crime>> reqCrimeAtLocationCall(@QueryMap Map<String,String> params);

        @GET("crimes-street/{category}")
        Call<ArrayList<Crime>> reqCrimeAroundLocation(@Path("category") String category,@QueryMap Map<String,String> params);

        @POST("crimes-street/{category}")
        Call<ArrayList<Crime>> reqCrimeInNeighbourhood(@Path("category")String category, @Query("poly")String poly,@Query("date") String date);

        @GET("{forceid}/neighbourhoods")
        Call<ArrayList<Neighbourhood>> reqNeighbourhoodCall(@Path("forceid") String forceid);

        @GET("{forceid}/{neighbourhoodid}/boundary")
        Call<ArrayList<Boundary>> reqNeighbourhoodBoundary(@Path("forceid") String forceid,@Path("neighbourhoodid") String neighbourhoodid);


    }

    public GetCrimes() {
        retrofit = new Retrofit.Builder()
                .baseUrl(CommonApiAdapter.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mapi = retrofit.create(API.class);
    }

    public Call<ArrayList<Crime>> getCrimesAtLocation(String date,String Lat,String Lng){
        Map<String,String> params = new HashMap<>();
        params.put("date",date);
        params.put("lat",Lat);
        params.put("lng",Lng);
        return mapi.reqCrimeAtLocationCall(params);
    }


    public Call<ArrayList<Crime>> getCrimesAroundLocationCall(String date,String Lat,String Lng,String Cat){
        Map<String,String> params = new HashMap<>();
        params.put("date",date);
        params.put("lat",Lat);
        params.put("lng",Lng);
        return mapi.reqCrimeAroundLocation(Cat,params);
    }

    public Call<ArrayList<Neighbourhood>> getNeighbourhoodsCall(String Forceid){
        return mapi.reqNeighbourhoodCall(Forceid);
    }

    public Call<ArrayList<Boundary>> getNeighbourhoodBoundariescall(String Forceid,String Neignbourhoodid){
        return mapi.reqNeighbourhoodBoundary(Forceid, Neignbourhoodid);
    }

    public Call<ArrayList<Crime>> getCrimesInNeighbourhood(String catogory, ArrayList<Boundary> boundaries, String date){
        String poly = "";
        for (Boundary B: boundaries ) {
            if(poly.equals("")){
                poly+= B.latitude+","+B.longitude;
            }
            else poly+= ":"+B.latitude+","+B.longitude;
        }
        return mapi.reqCrimeInNeighbourhood(catogory,poly,date);
    }

    public class Boundary{
        String latitude;
        String longitude;

        public Boundary(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
